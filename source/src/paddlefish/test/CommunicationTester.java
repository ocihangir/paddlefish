package paddlefish.test;

import java.io.IOException;

import paddlefish.protocol.CommController;

import paddlefish.protocol.CommConstants;

public class CommunicationTester {
	static CommController commCont;
	public static boolean testReadBytes(char deviceAddress, char registerAddress, int length) throws IOException, InterruptedException
	{
		char[] data = commCont.readByteArray(deviceAddress, registerAddress, length);
		byte[] buffer = new byte[data.length];
		for (int i=0;i<data.length;i++)
			buffer[i] = (byte) data[i];
		System.out.print("Data received : ");
		System.out.print(buffer[0] & 0xFF);
		System.out.print(" ");
		System.out.print(buffer[1] & 0xFF);
		System.out.print(" ");
		System.out.println(buffer[2] & 0xFF);
		if ((buffer[0] & 0xFF) != CommConstants.CMD_ANSWER)
		{
			System.out.print("Answer start byte is wrong!");
			return false;
		}
		if ((buffer[1] & 0xFF) != 0xE5)
		{
			System.out.print("ID is wrong!");
			return false;
		}
		if ((buffer[2] & 0xFF) != CommConstants.CMD_END)
		{
			System.out.print("Answer end byte wrong!");
			return false;
		}
		return true;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		commCont = new CommController();
		try {Thread.sleep(2000);} catch (InterruptedException ie) {}
		boolean tst = testReadBytes((char)0x53, (char)0x00, 1);
		if (tst)
			System.out.print("Communication test - OK!");
		else
			System.out.print("Communication test - FAILED!");
		try {Thread.sleep(10000);} catch (InterruptedException ie) {}
	}
}
