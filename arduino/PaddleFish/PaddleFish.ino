/* Paddlefish */

#include <Wire.h>
#include "TimerOne.h"

int ledb = 13;
int blinkCounter = 0;

void setup()
{
  // initialize the serial communication:
  Serial.begin(115200);
#ifdef LEONARDO
  Serial1.begin(9600);
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
  receiveBuffer[0] = i2c_read(devAddress);
  
  return receiveBuffer;
}

char i2c_read(char devAddress)
{
  // start I2C
  TWCR = (1<<TWINT) | (1<<TWSTA) |(1<<TWEN);
  
  // wait for command complete
  while (!(TWCR & (1<<TWINT)));
  
  // check if the start was successful
  if ((TWSR & 0xF8) != 0x08)
    digitalWrite( ledb, HIGH); //Error
    
  // read address
  char SLA_R = (devAddress << 1) | 1;
    
  // send address
  TWDR = SLA_R; 
  TWCR = (1<<TWINT) | (1<<TWEN);
  
  // wait for command complete
  while (!(TWCR & (1<<TWINT)));
  
  // address sent successful?
  if ((TWSR & 0xF8) != 0x40)
    digitalWrite( ledb, HIGH); //Error
  
  TWCR = (1<<TWINT) | (1<<TWEN);

  // wait for command complete
  while (!(TWCR & (1<<TWINT)));

  // read data successful?
  if ((TWSR & 0xF8) != 0x58)
    digitalWrite( ledb, HIGH); //Error
      
  TWCR = (1<<TWINT) | (0<<TWSTA) | (1<<TWSTO) | (1<<TWEN);
  
  return TWDR;
}

void i2c_write(char devAddress, char data)
{
  // start I2C
  TWCR = (1<<TWINT) | (1<<TWSTA) |(1<<TWEN);
  
  // wait for command complete
  while (!(TWCR & (1<<TWINT)));
  
  // check if the start was successful
  if ((TWSR & 0xF8) != 0x08)
    digitalWrite( ledb, HIGH); //Error
    
  // read address
  char SLA_W = (devAddress << 1);
    
  // send address
  TWDR = SLA_W; 
  TWCR = (1<<TWINT) | (1<<TWEN);
  
  // wait for command complete
  while (!(TWCR & (1<<TWINT)));
  
  // address sent successful?
  if ((TWSR & 0xF8) != 0x18)
    digitalWrite( ledb, HIGH); //Error
    
  TWDR = data;
  
  TWCR = (1<<TWINT) | (1<<TWEN);

  // wait for command complete
  while (!(TWCR & (1<<TWINT)));

  // read data successful?
  if ((TWSR & 0xF8) != 0x28)
    digitalWrite( ledb, HIGH); //Error
      
  TWCR = (1<<TWINT) | (0<<TWSTA) | (1<<TWSTO) | (1<<TWEN);
  
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
