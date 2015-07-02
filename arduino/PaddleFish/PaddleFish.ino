/* Paddlefish */

#include <Wire.h>
#include "TimerOne.h"

#define START_CONDITION 0x00
#define SEND_CONDITION 0x01
#define SEND_CONDITION_NACK 0x02
#define STOP_CONDITION 0x03
#define REPEATED_START_CONDITION 0x04

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
  char* recBuf = pfReadBytes(0x53,0x32,2);
  Serial.print("read buffer: ");
  Serial.println(recBuf[1],DEC);
  blinkLed();
}

/*
* read bytes from i2c device
*/
char* pfReadBytes(char devAddress, char regAddress, char length)
{
  i2c_write(devAddress,regAddress);
  if (i2c_tx(REPEATED_START_CONDITION) != 0x10)
    digitalWrite( ledb, HIGH); //Error
  char* receiveBuffer = i2c_read(devAddress,length);
  i2c_tx(STOP_CONDITION);
  
  return receiveBuffer;
}

char* i2c_read(char devAddress,char length)
{
  static char receiveBuffer[16];
  // slave address to be written
  char SLA_R = (devAddress << 1) | 1;
    
  // send address
  TWDR = SLA_R; 
  if (i2c_tx(SEND_CONDITION) != 0x40)
    digitalWrite( ledb, HIGH); //Error
  
  for(int dataCount=0;dataCount<length;dataCount++)
  {
    if ((dataCount!=length-1))
    {
      if (i2c_tx(SEND_CONDITION) != 0x50)
        digitalWrite( ledb, HIGH); //Error
    } else {
      if (i2c_tx(SEND_CONDITION_NACK) != 0x58)
        digitalWrite( ledb, HIGH); //Error
    }
    receiveBuffer[dataCount] = TWDR;
  }
  
  return receiveBuffer;
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

/*
* Transmit I2C command
* Communication start with START_CONDITION
* SEND_CONDITION transmits command following with an ACK
* SEND_CONDITION_NACK transmits command following with a NACK
* STOP_CONDITION stops the communication and releases I2C line
* REPEATED_START_CONDITION starts another communication without
* stoping the current one. It is required to read I2C device. Master
* writes to the device address and register address first. Then,
* after REPEATED_START_CONDITION, it listens the device.
*/
char i2c_tx(char mode)
{
  //TWCR: TWINT|TWEA|TWSTA|TWSTO|TWWC|TWEN|-|TWIE
  delay(0); // TODO: if this line is removed, the code won't work!!
  switch(mode)
  {
    case START_CONDITION:
      TWCR = (1<<TWINT) | (1<<TWSTA) | (1<<TWEN);
      break;
    case SEND_CONDITION:
      TWCR = (1<<TWINT) | (1<<TWEN) | (1<<TWEA);
      break;
    case SEND_CONDITION_NACK:
      TWCR = (1<<TWINT) | (1<<TWEN);
      break;
    case STOP_CONDITION:
      TWCR = (1<<TWINT) | (0<<TWSTA) | (1<<TWSTO) | (1<<TWEN);
      break;
    case REPEATED_START_CONDITION:
      TWCR = (1<<TWINT) | (1<<TWSTA) | (0<<TWSTO) | (1<<TWEN);
      break;
  }   
  
  if ((mode != STOP_CONDITION))
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
