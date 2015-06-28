/* Paddlefish */

#include <Wire.h>
#include "TimerOne.h"

int ledb = 13;
int blinkCounter = 0;
boolean blinkToggle = false;

void setup()
{
  // initialize the serial communication:
  Serial.begin(9600);
#ifdef LEONARDO
  Serial1.begin(9600);
   while (!Serial1) {
    ; // wait for Serial1 to be ready
  }
#endif
  
  // start i2c
  Wire.begin();
  
  // set timer to 10 milliseconds. It is constant for now.
  Timer1.initialize(10000);
  
  // attach timer interrupt
  Timer1.attachInterrupt(heartBeat);
  
  // heartbeat led
  pinMode(ledb, OUTPUT);
  digitalWrite(ledb,LOW);
}


void loop()
{
  // your code here...
  // caution!
  // don't use interrupts
  // don't use timer1
  
}

/*
* HeartBeat is called by timer1 interrupt.
*/
void heartBeat()
{
  //char* recBuf = pfReadBytes(0x53,0x00,1);
  blinkLed();
}

/*
* read bytes from i2c device
*/
char* pfReadBytes(char devAddress, char regAddress, char len)
{
  static char receiveBuffer[255];
  int i=0;
  
  // request data from device register
  Wire.beginTransmission(devAddress);
  Wire.write(regAddress);
  Wire.endTransmission();
  
  // read bytes
  Wire.requestFrom(devAddress, len);
  while(Wire.available())
  { 
    receiveBuffer[i++] = Wire.read();
    delay(10);
  }
  Wire.endTransmission();
}

/*
* Blink the LED 1 sec on - 1 sec off
* to indicate correct clock frequency.
* If it doesn't blink in this period,
* timer1 prescaler must be set accordingly.
*/
void blinkLed()
{
  if (blinkCounter>100) // the timer interrupt set to 10ms
  {
    blinkToggle=!blinkToggle;
    blinkCounter=0;
  }
    
  digitalWrite(ledb,blinkToggle);
  
  blinkCounter++;
}
