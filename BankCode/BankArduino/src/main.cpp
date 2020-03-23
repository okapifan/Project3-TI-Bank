#include <Arduino.h>


// Wait without delay
unsigned long previousMillis = 0;
const long interval = 4000;


// Functions
void SendString(String data);
char* ReceiveString();




void setup() {
  Serial.begin(9600);
}

void loop() {
  // Send a string
  SendString("Send");


  // Receive a string
  char* userInput = ReceiveString();
  if (userInput!=NULL)
  {
    // Do something with the received string

    // Send the string back
    SendString(userInput);
  }
}



void SendString(String data){
  Serial.print(data);
}

char* ReceiveString()
{
  static char data[21]; // For strings of max length=20
  if (!Serial.available()) return NULL;
  delay(64); // wait for all characters to arrive
  memset(data,0,sizeof(data)); // clear data
  byte count=0;
  while (Serial.available())
  {
    char c=Serial.read();
    if (c>=32 && count<sizeof(data)-1)
    {
      data[count]=c;
      count++;
    }
  }
  data[count]='\0'; // make it a zero terminated string
  return data;
}