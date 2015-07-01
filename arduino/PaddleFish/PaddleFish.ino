/* Paddlefish */

#include <Wire.h>
#include "TimerOne.h"

#define START_CONDITION 0x00
#define SEND_CONDITION 0x01
#define STOP_CONDITION 0x02
#define REPEATED_START_CONDITION 0x03

int ledb = 13;
int blinkCounter = 0;

void setup()
{
  // initialize the serial communication:
  Serial.begin(115200);
#ifdef LEONARDO
  Serial1.begin(115200);
   while (!Serial1) {
    ; // wait for Serial1 to be ready
  }
#endif
  
  // start i2c
  //Wire.begin();
  
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
  char* recBuf = pfReadBytes(0x53,0x00,1);
  Serial.print("read buffer: ");
  Serial.println(recBuf);
  blinkLed();
}

/*
* read bytes from i2c device
*/
char* pfReadBytes(char devAddress, char regAddress, char len)
{
  static char receiveBuffer[16];

  i2c_write(devAddress,regAddress);
  if (i2c_tx(REPEATED_START_CONDITION) != 0x10)
    digitalWrite( ledb, HIGH); //Error
  receiveBuffer[0] = i2c_read(devAddress);
  i2c_tx(STOP_CONDITION);
  
  return receiveBuffer;
}

char i2c_read(char devAddress)
{
  // slave address to be written
  char SLA_R = (devAddress << 1) | 1;
    
  // send address
  TWDR = SLA_R; 
  if (i2c_tx(SEND_CONDITION) != 0x40)
    digitalWrite( ledb, HIGH); //Error
  
  // read data
  if (i2c_tx(SEND_CONDITION) != 0x58)
    digitalWrite( ledb, HIGH); //Error
  
  return TWDR;
}

void i2c_write(char devAddress, char data)
{
  // start I2C
  if (i2c_tx(START_CONDITION) != 0x08)
    digitalWrite( ledb, HIGH); //Error
    
  // slave address to be read
  char SLA_W = (devAddress << 1);
    
  // send address
  TWDR = SLA_W; 
  if (i2c_tx(SEND_CONDITION) != 0x18)
    digitalWrite( ledb, HIGH); //Error
    
  // send data
  TWDR = data;
  if (i2c_tx(SEND_CONDITION) != 0x28)
    digitalWrite( ledb, HIGH); //Error      
}

char i2c_tx(char mode)
{
  delay(0); // TODO: if this line is removed, the code won't work!!
  switch(mode)
  {
    case START_CONDITION:
      TWCR = (1<<TWINT) | (1<<TWSTA) | (1<<TWEN);
      break;
    case SEND_CONDITION:
      TWCR = (1<<TWINT) | (1<<TWEN);
      break;
    case STOP_CONDITION:
      TWCR = (1<<TWINT) | (0<<TWSTA) | (1<<TWSTO) | (1<<TWEN);
      break;
    case REPEATED_START_CONDITION:
      TWCR = (1<<TWINT) | (1<<TWSTA) | (0<<TWSTO) | (1<<TWEN);
      break;
  }   
  
  if (mode != STOP_CONDITION)
    while (!(TWCR & (1<<TWINT)));// wait for command complete
    
  return (TWSR & 0xF8);
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
    digitalWrite( ledb, digitalRead( ledb ) ^ 1 );
    blinkCounter=0;
  }
  
  blinkCounter++;
}
