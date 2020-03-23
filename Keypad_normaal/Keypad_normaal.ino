#include <Keypad.h>

const byte ROWS= 4;

const byte COLS= 4;

char keys[ROWS][COLS]= {
{'1', '2', '3', 'A'},
{'4', '5', '6', 'B'},
{'7', '8', '9', 'C'},
{'*', '0', '#', 'D'}
};

byte rowPins[ROWS] = {2,3,4,5}; //Rows 0 to 3

byte colPins[COLS]= {6,7,8,9}; //Columns 0 to 3

//initializes an instance of the Keypad class

Keypad keypad= Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

void setup() {

Serial.begin(9600);

}

void loop() {

char key = keypad.getKey();

if (key){
Serial.print(key);
}
}
