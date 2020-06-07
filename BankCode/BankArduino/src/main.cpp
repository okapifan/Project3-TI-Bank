/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>
#include <Stepper.h>
#include <Servo.h>

//Printer
//#include "Adafruit_Thermal.h"
#include "SoftwareSerial.h"

// RFID
#define SS_PIN 53
#define RST_PIN 49
#define switch_card 48
MFRC522 mfrc522(SS_PIN, RST_PIN);


// Printer
/*#define TX_PIN 47
#define RX_PIN 45
SoftwareSerial mySerial(RX_PIN, TX_PIN); // Declare SoftwareSerial obj first
Adafruit_Thermal printer(&mySerial);     // Pass addr to printer constructor*/

// Servo
Servo myServo;
#define servoPin 35

// Fan
#define fan 10
 
// Steppemotor
#define STEPS 100
Stepper myStepper(STEPS, 37, 39, 41, 43);

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
void GetBill(int distance);

void setup()
{
	Serial.begin(9600);
	SPI.begin();
	mfrc522.PCD_Init();

	// RFID
	pinMode (switch_card, INPUT_PULLUP);

	// Printer
  	//mySerial.begin(9600);  // Initialize SoftwareSerial
  	//printer.begin();       // Init printer (same regardless of serial type)

	// Servo
	myServo.attach(servoPin);
	
	// Fan
  	pinMode(fan, OUTPUT);

	// Steppermotor
	myStepper.setSpeed(300);

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
			//SendString("Cin");

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
		Serial.println("M" + receivedString);
		if (receivedString.substring(0,1) == "D") {
    		DispensMoney(receivedString.substring(1));
		}
		else if (receivedString.substring(0,1) == "P") {
    		PrintReceipt(receivedString.substring(1));
		}
		
		
		// Send string example
		// SendString(userInput);
	}
}
	

void DispensMoney(String geld) // D1-1-1-1
{
	Serial.println("MDispensMoney");

	// D: Dispence money (amount $50 bills, amount $20 bills, amount $10 bills, amount $5 bills)(1-0-2-0)

	/*char array[50];
	geld.toCharArray(array, 50);

	char *strings[10];
	char *ptr = NULL;
	byte index = 0;

	ptr = strtok(array, "-"); // Takes a list of delimiters
	while (ptr != NULL)
	{
		strings[index] = ptr;
		index++;
		ptr = strtok(NULL, "-"); // Takes a list of delimiters
	}*/


	String bill50a = geld.substring(0,1);
	int bill50 = bill50a.toInt();
	String bill20a = geld.substring(2,3);
	int bill20 = bill20a.toInt();
	String bill10a = geld.substring(4,5);
	int bill10 = bill10a.toInt();
	String bill5a = geld.substring(6,7);
	int bill5 = bill5a.toInt();
	
	// Voeg dispenser toe en zorg dat hij verschillende briefjes kan dispensen

	/*
	for (int i = 0; i < bill50; i++)
	{
		GetBill(?);
	}
	*/
	
	for (int i = 0; i < bill20; i++)
	{
		Serial.println("MDispensMoney: GetBill 20");
		GetBill(4600);
	}
	
	for (int i = 0; i <bill10; i++)
	{
		Serial.println("MDispensMoney: GetBill 10");
		GetBill(2850);
	}

	for (int i = 0; i < bill5; i++)
	{
		Serial.println("MDispensMoney: GetBill 5");
		GetBill(1350);
	}

	// send D
	SendString("D");
}

void GetBill(int distance)
{
	// Move servo up
	for (int r = 160; r > 75; r--)
	{
		myServo.write(r);
		delay(20);
	}

	// Move to bill place
	myStepper.step(distance);

	// Move servo down
	for (int i = 75; i < 160; i++)
	{
		myServo.write(i);
		delay(20);
	}

	// Turn fan on
	digitalWrite(fan, HIGH);
	delay(3000);

	// Move servo up
	for (int r = 160; r > 75; r--)
	{
		myServo.write(r);
		delay(20);
	}

	// Move to bill place
	myStepper.step(-(distance + 100));

	// Move servo down
	for (int i = 75; i < 160; i++)
	{
		myServo.write(i);
		delay(20);
	}

	// Turn fan off
	digitalWrite(fan, LOW);
	delay(3000);
}


void PrintReceipt(String data){
	// P: Print bon (Time-pinValue-accountnNr-balance)(Sat May 23 13:58:45 CEST 2020-65-00001234-180)

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
	
	// Print information on receipt printer
	/*printer.setSize('L'); // Size large
	printer.justify('C'); // Print in center
	printer.println(F("TimoBank"));
	printer.setSize('S'); // Size small
	printer.justify('R'); // Print at the right	
	printer.println("Locatie: US");
	printer.println("Transactie soort: geld opnemen");
	printer.println("Hoeveelheid: " + (String) strings[0]);
	printer.println("Account nummer: " + (String) strings[1]);
	printer.println("Datum: " + (String) strings[2]);
	printer.justify('C'); // Print in center
	printer.println(F("Bedankt voor het gebruiken van onze geldautomaat"));*/
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