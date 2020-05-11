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

MFRC522::MIFARE_Key keyRFID;

// Data location
const byte block = 1;

//*****************************************************************************************//
void setup()
{
	Serial.begin(9600);										   // Initialize serial communications with the PC
	SPI.begin();											   // Init SPI bus
	mfrc522.PCD_Init();										   // Init MFRC522 card
	Serial.println(F("Read personal data on a MIFARE PICC:")); //shows in serial that it is ready to read

	// RFID read key
	keyRFID.keyByte[0] = 0xFF;
	keyRFID.keyByte[1] = 0xFF;
	keyRFID.keyByte[2] = 0xFF;
	keyRFID.keyByte[3] = 0xFF;
	keyRFID.keyByte[4] = 0xFF;
	keyRFID.keyByte[5] = 0xFF;
}

//*****************************************************************************************//
void loop()
{
	//some variables we need
	byte len = 18;
	MFRC522::StatusCode status;

	//-------------------------------------------

	// Reset the loop if no new card present on the sensor/reader. This saves the entire process when idle.
	if (!mfrc522.PICC_IsNewCardPresent())
	{
		return;
	}

	// Select one of the cards
	if (!mfrc522.PICC_ReadCardSerial())
	{
		return;
	}

	Serial.println(F("**Card Detected:**"));

	//-------------------------------------------

	byte buffer2[18];

	status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, 1, &keyRFID, &(mfrc522.uid));
	if (status != MFRC522::STATUS_OK)
	{
		Serial.print(F("Authentication failed: "));
		Serial.println(mfrc522.GetStatusCodeName(status));
		return;
	}

	status = mfrc522.MIFARE_Read(block, buffer2, &len);
	if (status != MFRC522::STATUS_OK)
	{
		Serial.print(F("Reading failed: "));
		Serial.println(mfrc522.GetStatusCodeName(status));
		return;
	}

	// Print
	for (uint8_t i = 0; i < 16; i++)
	{
		Serial.write(buffer2[i]);
	}

	//----------------------------------------

	Serial.println(F("\n**End Reading**\n"));

	delay(1000); //change value if you want to read cards faster

	mfrc522.PICC_HaltA();
	mfrc522.PCD_StopCrypto1();
}