/*
 * Write personal data of a MIFARE RFID card using a RFID-RC522 reader
 * Uses MFRC522 - Library to use ARDUINO RFID MODULE KIT 13.56 MHZ WITH TAGS SPI W AND R BY COOQROBOT. 
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
 *
 * Hardware required:
 * Arduino
 * PCD (Proximity Coupling Device): NXP MFRC522 Contactless Reader IC
 * PICC (Proximity Integrated Circuit Card): A card or tag using the ISO 14443A interface, eg Mifare or NTAG203.
 * The reader can be found on eBay for around 5 dollars. Search for "mf-rc522" on ebay.com. 
 */

// Example: RUS-TIMO-00001234
// 00001234 D
// 00008467 Z
// 00004727 T
// 00007236 R


#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>

#define RST_PIN 49           // Configurable, see typical pin layout above
#define SS_PIN 53  // Configurable, see typical pin layout above

MFRC522 mfrc522(SS_PIN, RST_PIN); // Create MFRC522 instance

MFRC522::MIFARE_Key keyRFID;

// Data location
const byte block = 1;

//*****************************************************************************************//
void setup()
{
	Serial.begin(9600); // Initialize serial communications with the PC
	SPI.begin();		// Init SPI bus
	mfrc522.PCD_Init(); // Init MFRC522 card
	Serial.println(F("Write data on a MIFARE RFID card"));

	// RFID write key
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

	// Print card information
	Serial.print(F("Card UID:")); //Dump UID
	for (byte i = 0; i < mfrc522.uid.size; i++)
	{
		Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
		Serial.print(mfrc522.uid.uidByte[i], HEX);
	}
	Serial.print(F(" PICC type: ")); // Dump PICC type
	MFRC522::PICC_Type piccType = mfrc522.PICC_GetType(mfrc522.uid.sak);
	Serial.println(mfrc522.PICC_GetTypeName(piccType));



	byte buffer[16];
	MFRC522::StatusCode status;
	byte len;

	Serial.setTimeout(20000L); // wait until 20 seconds for input from serial
	// Ask personal data: Family name
	Serial.println(F("Type data")); // End with #
	Serial.println(F("Example: US-TIMO-00001234"));
	len = Serial.readBytesUntil('#', (char *)buffer, 16);
	for (byte i = len; i < 30; i++)
		buffer[i] = ' '; // pad with spaces


	//Serial.println(F("Authenticating using key A..."));
	status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &keyRFID, &(mfrc522.uid));
	if (status != MFRC522::STATUS_OK)
	{
		Serial.print(F("PCD_Authenticate() failed: "));
		Serial.println(mfrc522.GetStatusCodeName(status));
		return;
	}
	else
	{
		Serial.println(F("PCD_Authenticate() success: "));
	}

	// Write block
	status = mfrc522.MIFARE_Write(block, buffer, 16);
	if (status != MFRC522::STATUS_OK)
	{
		Serial.print(F("MIFARE_Write() failed: "));
		Serial.println(mfrc522.GetStatusCodeName(status));
		return;
	}
	else
	{
		Serial.println(F("MIFARE_Write() success: "));
	}

	Serial.println(" ");
	mfrc522.PICC_HaltA();	   // Halt PICC
	mfrc522.PCD_StopCrypto1(); // Stop encryption on PCD
}