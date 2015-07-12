package paddlefish.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import paddlefish.def.SensorCategory;
import paddlefish.protocol.CommController;
import paddlefish.protocol.CommConstants;
import paddlefish.sensor.ADXL345;

public class CommunicationTester 
{
	static CommController commCont;
	public static byte[] testReadBytes(char deviceAddress, char registerAddress, int length) throws IOException, InterruptedException
	{
		char[] data = commCont.readByteArray(deviceAddress, registerAddress, length);
		byte[] buffer = new byte[data.length];
		for (int i=0;i<data.length;i++)
			buffer[i] = (byte) data[i];
		System.out.print("Data received : ");
		for (int i = 0;i<length+2;i++){
			System.out.print(buffer[i] & 0xFF); 
			System.out.print(" ");
		}
		System.out.println(" ");
		
		return buffer;
	}
	
	public static void testWriteSingleByte(char deviceAddress, char registerAddress, char data) throws IOException, InterruptedException
	{
		commCont.writeSingleByte(deviceAddress, registerAddress, data);
		System.out.println("Data written");		
	}
	
	public static void testWriteMultiBytes(char deviceAddress, char registerAddress, char[] data) throws IOException, InterruptedException
	{
		commCont.writeByteArray(deviceAddress, registerAddress, data.length, data);
		System.out.println("Data written");		
	}
	
	public static boolean testReadWriteMultiBytes() throws IOException, InterruptedException
	{
		System.out.println("-Testing reading and writing multiple bytes-");
		
		System.out.println("Reading ADXL345 multiple addresses 0x1E 0x1F 0x20...");
		testReadBytes((char)0x53, (char)0x1E, 3);
		
		char[] data = {0x55, 0x55, 0x55};
		System.out.println("Writing 0x55 to ADXL345 0x1E 0x1F 0x20 address...");
		testWriteMultiBytes((char)0x53, (char)0x1E, data);
		
		System.out.println("Reading ADXL345 multiple addresses 0x1E 0x1F 0x20...");
		byte[] res = testReadBytes((char)0x53, (char)0x1E, 3);
		
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if (res[1] != 0x55)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[2] != 0x55)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[3] != 0x55)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if ((res[4] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		
		char[] data2 = {0x00, 0x00, 0x00};
		System.out.println("Writing 0x00 to ADXL345 0x1E 0x1F 0x20 address...");
		testWriteMultiBytes((char)0x53, (char)0x1E, data2);
		
		System.out.println("Reading ADXL345 multiple addresses 0x1E 0x1F 0x20...");
		res = testReadBytes((char)0x53, (char)0x1E, 3);
		
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if (res[1] != 0x00)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[2] != 0x00)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[3] != 0x00)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if ((res[4] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		
		System.out.println("\n");
		
		return true;
	}
	
	public static boolean testReadWriteSingleByte() throws IOException, InterruptedException
	{
		System.out.println("-Testing reading and writing single byte-");
		
		System.out.println("Reading ADXL345 0x2D address...");
		testReadBytes((char)0x53, (char)0x2D, 1);
		
		System.out.println("Writing 0x08 to ADXL345 0x2D address...");
		testWriteSingleByte((char)0x53,(char)0x2D,(char)0x08);
		
		System.out.println("Reading back ADXL345 0x2D address...");
		byte[] res = testReadBytes((char)0x53, (char)0x2D, 1);
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if (res[1] != 0x08)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if ((res[2] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		
		System.out.println("Writing 0x00 to ADXL345 0x2D address...");
		testWriteSingleByte((char)0x53,(char)0x2D,(char)0x00);
		
		System.out.println("Reading back ADXL345 0x2D address...");
		res = testReadBytes((char)0x53, (char)0x2D, 1);
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if (res[1] != 0x00)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if ((res[2] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		
		System.out.println("\n");
		
		return true;
	}	
	
	public static boolean testADXL345ID() throws Exception
	{
		System.out.println("-Testing reading ADXL345 ID-");
		
		System.out.println("Reading ADXL345 ID...");
		
		ADXL345 adSens = new ADXL345(SensorCategory.ACC, "ADXL345");
		
		char hexAdd = (char) (adSens.getI2cInf().getActiveDeviceAddr()&0xff);
		char devIdAdd =  (char) (adSens.getIdentInfo().deviceIDAddress&0xff);
		byte devId = adSens.getIdentInfo().deviceID;
		
		byte[] res = testReadBytes(hexAdd, devIdAdd, 1);
		
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if ((byte)(res[1] & 0xFF) != devId)
		{
			System.out.print("ID is wrong!");
			return false;
		}
		if ((res[2] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		
		System.out.println("\n");
		
		return true;
	}
	
	
	public static boolean testPower(CommController com) throws Exception
	{
		System.out.println("-Testing Power ADXL345-");
		
		ADXL345 adSens = new ADXL345(SensorCategory.ACC, "ADXL345");
		
		char hexAdd = (char) (adSens.getI2cInf().getActiveDeviceAddr()&0xff);
		
		byte[] sensorVal = testReadBytes(hexAdd, (char) 0x32, 0x06);

		adSens.powerDown();
		Thread.sleep(1000);
		adSens.powerUp();
		sensorVal = testReadBytes(hexAdd, (char) 0x32, 0x06);
		adSens.powerDown();
		Thread.sleep(1000);
		sensorVal = testReadBytes(hexAdd, (char) 0x32, 0x06);
		
		System.out.println("\n");
		
		return true;
	}
	
	public static boolean testEEPROMReadWriteMultiBytes() throws IOException, InterruptedException
	{
		System.out.println("-Testing EEPROM reading and writing multiple bytes-");
		
		System.out.println("Reading multiple addresses 0x1E 0x1F 0x20...");
		testReadBytes((char)0x50, (char)0x00, 3);
		
		Thread.sleep(300);
		
		char[] data = {0x55, 0x55, 0x55};
		System.out.println("Writing 0x55 to 0x1E 0x1F 0x20 address...");
		testWriteMultiBytes((char)0x50, (char)0x00, data);
		
		Thread.sleep(300);
		
		System.out.println("Reading multiple addresses 0x1E 0x1F 0x20...");
		byte[] res = testReadBytes((char)0x50, (char)0x00, 3);
		
		
		
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if (res[1] != 0x55)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[2] != 0x55)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[3] != 0x55)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if ((res[4] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		
		Thread.sleep(300);
		
		char[] data2 = {0x00, 0x00, 0x00};
		System.out.println("Writing 0x00 to 0x1E 0x1F 0x20 address...");
		testWriteMultiBytes((char)0x50, (char)0x00, data2);
		
		Thread.sleep(300);
		
		System.out.println("Reading multiple addresses 0x1E 0x1F 0x20...");
		res = testReadBytes((char)0x50, (char)0x00, 3);
		
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if (res[1] != 0x00)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[2] != 0x00)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if (res[3] != 0x00)
		{
			System.out.print("Written and read data don't match!");
			return false;
		}
		if ((res[4] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		
		System.out.println("\n");
		
		return true;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		/* */
		commCont = CommController.getInstance();
		try {Thread.sleep(2000);} catch (InterruptedException ie) {} // Wait for communication channel is up
		
		boolean tst = true;
		
		//tst &= testADXL345ID();
		
		//tst &= testReadWriteSingleByte();
		
		//tst &= testReadWriteMultiBytes();
		
		Thread.sleep(100);
		
		tst &= testEEPROMReadWriteMultiBytes();
		
		tst&= testPower(commCont);
		
		commCont.close();
		
		if (tst)
			System.out.print("Communication test - OK!");
		else
			System.out.print("Communication test - FAILED!");
	}
}
