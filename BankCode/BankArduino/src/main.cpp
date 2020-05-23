#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>
#include <Stepper.h>

// RFID
#define STEPS 32
#define SS_PIN 10
#define RST_PIN A0
#define switch_card 22
MFRC522 mfrc522(SS_PIN, RST_PIN);


// Printer
#define TXD 49
#define RXD 47

//Servo
#define Pulse 37
 
// Steppemotor
Stepper stepper(STEPS, 39, 41, 43, 45);

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

//initializes an instance of the Keypad class
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
	pinMode (switch_card, INPUT_PULLUP);

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
	//Serial.print("_");

	char key = keypad.getKey();

	if (key)
	{
		SendString("K" + String(key));
	}


	// Look for new RFID cards
	bool newCard = mfrc522.PICC_IsNewCardPresent();
	bool readCard = mfrc522.PICC_ReadCardSerial();
	if (newCard && readCard)
	{
		//Serial.print("-1-");

		byte buffer[18];
		MFRC522::StatusCode status;
		byte len = 18;
		
		status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, 1, &keyRFID, &(mfrc522.uid));
		if (status != MFRC522::STATUS_OK)
		{
			//Serial.print(F("Authentication failed: "));
			//Serial.println(mfrc522.GetStatusCodeName(status));
			return;
		}

		status = mfrc522.MIFARE_Read(block, buffer, &len);
		if (status != MFRC522::STATUS_OK)
		{
			//Serial.print(F("Reading failed: "));
			//Serial.println(mfrc522.GetStatusCodeName(status));
			return;
		}

		String cardData = "";
		for (uint8_t i = 0; i < 16; i++)
		{
			cardData += (char)buffer[i];
		}
		SendString("R" + cardData); //SendString("RUS-TIMO-00001234");
		
		//Serial.print("-2-");

		mfrc522.PICC_HaltA();
		mfrc522.PCD_StopCrypto1();
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
		
		
		// Do something with the received string

		// Send the string back to test communication
		SendString(userInput);
	}


	if (digitalRead (switch_card) != ChangeCardState){
		
		ChangeCardState = digitalRead (switch_card);

		if(!ChangeCardState){	
			SendString("Cin");
		}
		else{
			SendString("Cout");
		}
		
	}
}
	

void DispensMoney(String geld){

	char array[50]; 
	geld.toCharArray(array,50);
	
	char *strings[10];
	char *ptr = NULL;
	byte index = 0;

		ptr = strtok(array, "-");  // takes a list of delimiters
		while(ptr != NULL)
		{
			strings[index] = ptr;
			index++;
			ptr = strtok(NULL, "-");  // takes a list of delimiters
		}
	Serial.println(strings[0]);
	Serial.println(strings[1]);
	Serial.println(strings[2]);
	//voeg dispenser toe en zorg dat hij verschillende briefjes kan dispensen
	//(int)strings[0]
}


void PrintReceipt(String data){

	char array[50]; 
	data.toCharArray(array,50);
	char *strings[10];
	char *ptr = NULL;
	byte index = 0;

		ptr = strtok(array, "-");  // takes a list of delimiters
		while(ptr != NULL)
		{
			strings[index] = ptr;
			index++;
			ptr = strtok(NULL, "-");  // takes a list of delimiters
		}
	
	//voeg bonnetjes printer toe en print deze informatie
	//(int)strings[0]
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
	delay(64);					   // wait for all characters to arrive
	memset(data, 0, sizeof(data)); // clear data
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
	data[count] = '\0'; // make it a zero terminated string
	return data;
}