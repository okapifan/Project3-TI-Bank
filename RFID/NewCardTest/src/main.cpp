/*
 * Initial Author: ryand1011 (https://github.com/ryand1011)
 *
 * Reads data written by a program such as "rfid_write_personal_data.ino"
 *
 * See: https://github.com/miguelbalboa/rfid/tree/master/examples/rfid_write_personal_data
 *
 * Uses MIFARE RFID card using RFID-RC522 reader
 * Uses MFRC522 - Library
 * -----------------------------------------------------------------------------------------
 *             MFRC522      Arduino       Arduino   Arduino    Arduino          Arduino
 *             Reader/PCD   Uno/101       Mega      Nano v3    Leonardo/Micro   Pro Micro
 * Signal      Pin          Pin           Pin       Pin        Pin              Pin
 * -----------------------------------------------------------------------------------------
 * RST/Reset   RST          9             5         D9         RESET/ICSP-5     RST
 * SPI SS      SDA(SS)      10            53        D10        10               10
 * SPI MOSI    MOSI         11 / ICSP-4   51        D11        ICSP-4           16
 * SPI MISO    MISO         12 / ICSP-1   50        D12        ICSP-1           14
 * SPI SCK     SCK          13 / ICSP-3   52        D13        ICSP-3           15
*/
#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>

#define RST_PIN A0 //9           // Configurable, see typical pin layout above
#define SS_PIN 10  // Configurable, see typical pin layout above

MFRC522 mfrc522(SS_PIN, RST_PIN); // Create MFRC522 instance

//*****************************************************************************************//
void setup()
{
	Serial.begin(9600);									  // Initialize serial communications with the PC
	SPI.begin();										  // Init SPI bus
	mfrc522.PCD_Init();									  // Init MFRC522 card
	Serial.println(F("Test new card on a MIFARE PICC:")); //shows in serial that it is ready to read
}

//*****************************************************************************************//
void loop()
{
	bool newCard = mfrc522.PICC_IsNewCardPresent();
	bool readCard = mfrc522.PICC_ReadCardSerial();
	Serial.println("Status: " + (String)newCard + ", " + (String)readCard);

	if (newCard && readCard)
	{
		Serial.println(F("**New Card Detected 1**"));
	}

	// Reset the loop if no new card present on the sensor/reader. This saves the entire process when idle.
	if (!newCard)
	{
		return;
	}
	// Select one of the cards
	if (!readCard)
	{
		return;
	}
	Serial.println(F("**New Card Detected 2**"));

	//delay(1000);

	mfrc522.PICC_HaltA();
	mfrc522.PCD_StopCrypto1();
}