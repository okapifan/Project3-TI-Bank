#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>

#define SS_PIN 10
#define RST_PIN A0
MFRC522 mfrc522(SS_PIN, RST_PIN);



// Defining the keypad
int thresholds[16] = {2, 77, 144, 202, 244, 290, 331, 368, 394, 424, 452, 477, 496, 518, 538, 566};
char keypad[16] = {'1', '2', '3', 'A', '4', '5', '6', 'B', '7', '8', '9', 'C', '*', '0', '#', 'D'};


//initializes an instance of the Keypad class

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
 int value = analogRead(A1);
// Serial.println(value);
// delay(100);

for (int i = 0; i < 16; i++)
  {
    
    //Is A1 Close enough to one of the keypad values?
    if (abs(value - thresholds[i]) < 5)
    {
      //Yes, translate the index of that value to the actual name of the key
      Serial.println(keypad[i]);

      //Wait until they releas the button
      while (analogRead(A1) < 1000) 
      {
        delay(100);
      }
    }
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