package paddlefish.test;

import java.io.IOException;

import paddlefish.protocol.CommController;

import paddlefish.protocol.CommConstants;

public class CommunicationTester {
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
	
	public static boolean testADXL345ID() throws IOException, InterruptedException
	{
		System.out.println("-Testing reading ADXL345 ID-");
		
		System.out.println("Reading ADXL345 ID...");
		byte[] res = testReadBytes((char)0x53, (char)0x00, 1);
		if ((res[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if ((res[1] & 0xFF) != 0xE5)
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
	
	public static void main(String[] args) throws Exception
	{
		commCont = new CommController();
		try {Thread.sleep(2000);} catch (InterruptedException ie) {} // Wait for communication channel is up
		
		boolean tst = true;
		
		tst &= testADXL345ID();
		
		tst &= testReadWriteSingleByte();
		
		tst &= testReadWriteMultiBytes();
		
		if (tst)
			System.out.print("Communication test - OK!");
		else
			System.out.print("Communication test - FAILED!");
	}
}
