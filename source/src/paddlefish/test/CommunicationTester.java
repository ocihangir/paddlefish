package paddlefish.test;

import java.io.IOException;

import paddlefish.protocol.CommController;

public class CommunicationTester {
	static CommController commCont;
	public static void testReadBytes(char deviceAddress, char registerAddress, int length) throws IOException, InterruptedException
	{
		char[] data = commCont.readByteArray(deviceAddress, registerAddress, length);
		byte[] buffer = new byte[data.length];
		for (int i=0;i<data.length;i++)
			buffer[i] = (byte) data[i];
		System.out.println("Data received : ");
		System.out.print(buffer[0] & 0xFF);
	}
	
	
	public static void main(String[] args) throws Exception
	{
		commCont = new CommController();
		try {Thread.sleep(2000);} catch (InterruptedException ie) {}
		testReadBytes((char)0x53, (char)0x00, 1);
		try {Thread.sleep(10000);} catch (InterruptedException ie) {}
	}
}
