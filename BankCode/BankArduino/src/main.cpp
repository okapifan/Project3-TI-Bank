#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>
#include <Stepper.h>

// RFID
#define SS_PIN 53
#define RST_PIN 49
#define switch_card 48
MFRC522 mfrc522(SS_PIN, RST_PIN);


// Printer
#define TXD 47
#define RXD 45

// Servo
#define Pulse 35
 
// Steppemotor
#define STEPS 32
Stepper stepper(STEPS, 37, 39, 41, 43);

// Data location
const byte block = 1;

MFRC522::MIFARE_Key keyRFID;


// Defining the keypad
const byte ROWS = 4;

const byte COLS = 4;

char keys[ROWS][COLS] = {
	{'1', '2', '3', 'A'},
	{'4', '5', '6', 'B'},
	{'7', '8', '9', 'C'},
	{'*', '0', '#', 'D'}};

byte rowPins[ROWS] = {2, 3, 4, 5}; //Rows 0 to 3

byte colPins[COLS] = {6, 7, 8, 9}; //Columns 0 to 3

// Initializes an instance of the Keypad class
Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

// Wait without delay
unsigned long previousMillis = 0;
const long interval = 4000;

boolean ChangeCardState; 

// Functions
void SendString(String data);
char *ReceiveString();
void DispensMoney(String geld);
void PrintReceipt(String data);

void setup()
{
	Serial.begin(9600);
	SPI.begin();
	mfrc522.PCD_Init();
	// RFID
	pinMode (switch_card, INPUT_PULLUP);
	pinMode (SS_PIN, OUTPUT)
	pinMode (RST_PIN, OUTPUT)
	//Steppemotor
	pinMode (STEPS, OUTPUT)
	pinMode (37, OUTPUT)
	pinMode (39, OUTPUT) 
	pinMode (41, OUTPUT)
	pinMode (43, OUTPUT)
	// Printer
	pinMode (TXD, OUTPUT)
	pinMode (RXD, OUTPUT)
	// Servo
	pinMode (Pulse, OUTPUT)

	stepper.setSpeed(200);

	// RFID read key
	keyRFID.keyByte[0] = 0xFF;
	keyRFID.keyByte[1] = 0xFF;
	keyRFID.keyByte[2] = 0xFF;
	keyRFID.keyByte[3] = 0xFF;
	keyRFID.keyByte[4] = 0xFF;
	keyRFID.keyByte[5] = 0xFF;

	
}

void loop()
{
	// Serial.print("_");

	char key = keypad.getKey();

	if (key)
	{
		SendString("K" + String(key));
	}


	

	if (digitalRead (switch_card) != ChangeCardState){
		
		ChangeCardState = digitalRead (switch_card);

		if(!ChangeCardState){	
			SendString("Cin");

			// Look for new RFID cards
			bool newCard = mfrc522.PICC_IsNewCardPresent();
			bool readCard = mfrc522.PICC_ReadCardSerial();
			if (newCard && readCard){
				byte buffer[18];
				MFRC522::StatusCode status;
				byte len = 18;
				
				status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, 1, &keyRFID, &(mfrc522.uid));
				if (status != MFRC522::STATUS_OK)
				{
					// Serial.print(F("Authentication failed: "));
					// Serial.println(mfrc522.GetStatusCodeName(status));
					return;
				}

				status = mfrc522.MIFARE_Read(block, buffer, &len);
				if (status != MFRC522::STATUS_OK)
				{
					// Serial.print(F("Reading failed: "));
					// Serial.println(mfrc522.GetStatusCodeName(status));
					return;
				}

				String cardData = "";
				for (uint8_t i = 0; i < 16; i++)
				{
					cardData += (char)buffer[i];
				}
				SendString("R" + cardData); // SendString("RUS-TIMO-00001234");

				mfrc522.PICC_HaltA();
				mfrc522.PCD_StopCrypto1();
			}
		}
		else{
			SendString("Cout");
		}
		
	}


	// Receive a string
	char *userInput = ReceiveString();
	if (userInput != NULL)
	{
		
		String receivedString = userInput;
		if (receivedString.substring(0,1) == "D") {
    		DispensMoney(receivedString.substring(2));
		}
		else if (receivedString.substring(0,1) == "P") {
    		PrintReceipt(receivedString.substring(2));
		}
		
		
		// Send string example
		// SendString(userInput);
	}
}
	

void DispensMoney(String geld){

	char array[50]; 
	geld.toCharArray(array,50);
	
	char *strings[10];
	char *ptr = NULL;
	byte index = 0;

		ptr = strtok(array, "-");  // Takes a list of delimiters
		while(ptr != NULL)
		{
			strings[index] = ptr;
			index++;
			ptr = strtok(NULL, "-");  // Takes a list of delimiters
		}
	Serial.println(strings[0]);
	Serial.println(strings[1]);
	Serial.println(strings[2]);
	// Voeg dispenser toe en zorg dat hij verschillende briefjes kan dispensen
	// (int)strings[0]
}


void PrintReceipt(String data){

	char array[50]; 
	data.toCharArray(array,50);
	char *strings[10];
	char *ptr = NULL;
	byte index = 0;

		ptr = strtok(array, "-");  // Takes a list of delimiters
		while(ptr != NULL)
		{
			strings[index] = ptr;
			index++;
			ptr = strtok(NULL, "-");  // Takes a list of delimiters
		}
	
	// Voeg bonnetjes printer toe en print deze informatie
	// (int)strings[0]
}


void SendString(String data)
{
	Serial.print(data);
}

char *ReceiveString()
{
	static char data[21]; // For strings of max length=20
	if (!Serial.available())
		return NULL;
	delay(64);					   // Wait for all characters to arrive
	memset(data, 0, sizeof(data)); // Clear data
	byte count = 0;
	while (Serial.available())
	{
		char c = Serial.read();
		if (c >= 32 && count < sizeof(data) - 1)
		{
			data[count] = c;
			count++;
		}
	}
	data[count] = '\0'; // Make it a zero terminated string
	return data;
}