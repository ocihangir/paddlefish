package paddlefish.hal;

import java.io.IOException;


public class HAL {
	USB usbComm;
	
	public HAL() throws Exception
	{
		// Linux - Ubuntu
		//usbComm = new USB("/dev/ttyACM1",115200);
		// Windows
		usbComm = new USB("COM4",115200);
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

	public void close()
	{
		if(this.usbComm!=null)
			this.usbComm.close();
		//TODO: Log
		else
			System.out.println("No UsbComm available");
	}
}
