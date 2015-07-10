package paddlefish.hal;

import java.io.IOException;


public class HAL {
	USB usbComm;
	public HAL() throws Exception
	{
		usbComm = new USB("/dev/ttyACM0",115200);
	}
	
	public void txData(char data[]) throws IOException
	{
		usbComm.sendData(data);
	}
	
	public char[] rxData() throws IOException
	{
		
		byte[] data = usbComm.receiveData();
		char[] buffer = new char[data.length];
		for (int i=0;i<data.length;i++)
			buffer[i] = (char) data[i];
		return buffer;
	}
}
