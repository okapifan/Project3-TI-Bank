//
//Reading 4x5 keypad with ONE Arduino Pin
//

int thresholds[16] = {2, 77, 144, 202, 244, 290, 331, 368, 394, 424, 452, 477, 496, 518, 538, 566};
Char keypad[16] = {'1', '2', '3', 'A', '4', '5', '6', 'B', '7', '8', '9', 'C', '*', '0', '#', 'D'};


void setup() {
  Serial.begin(9600);
 
}

void loop() {
 int value = analogRead(A1);
// Serial.println(value);
// delay(100);

for (int i = 0; i < 16; i++)
  {
    //Is A1 Close enough to one of the keypad values?
    if (abs(value - tresholds[i] < 5)
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
 }
