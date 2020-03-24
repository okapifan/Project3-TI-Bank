#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>

#define SS_PIN 10
#define RST_PIN A0
MFRC522 mfrc522(SS_PIN, RST_PIN);

// Defining the keypad
const byte ROWS = 4;

const byte COLS = 4;

char keys[ROWS][COLS] = {
{'1', '2', '3', 'A'},
{'4', '5', '6', 'B'},
{'7', '8', '9', 'C'},
{'*', '0', '#', 'D'}
};

byte rowPins[ROWS] = {2,3,4,5}; //Rows 0 to 3

byte colPins[COLS] = {6,7,8,9}; //Columns 0 to 3

//initializes an instance of the Keypad class
Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

// Wait without delay
unsigned long previousMillis = 0;
const long interval = 4000;

// Functions
void SendString(String data);
char *ReceiveString();

void setup()
{
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();
}

void loop()
{
	char key = keypad.getKey();

	if (key){
		void SendString(Key);
	}

  //look for new cards
  if (mfrc522.PICC_IsNewCardPresent())
  {
    if (mfrc522.PICC_ReadCardSerial())
    {
      //Show UID on serial monitor
      String content = "";

      for (byte i = 0; i < mfrc522.uid.size; i++)
      {
        content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? "0" : ""));
        content.concat(String(mfrc522.uid.uidByte[i], HEX));
      }
      SendString(content);
    }
  }


  // Send a string
  //SendString("Send");

  // Receive a string
  char *userInput = ReceiveString();
  if (userInput != NULL)
  {
    // Do something with the received string

    // Send the string back
    SendString(userInput);
  }
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
  delay(64);                     // wait for all characters to arrive
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