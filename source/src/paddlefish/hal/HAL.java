package paddlefish.hal;

import java.io.IOException;


public class HAL {
	USB usbComm;
	
	public HAL() throws Exception
	{
		// Linux - Ubuntu
		usbComm = new USB("/dev/ttyACM0",115200);
		// Windows
		//usbComm = new USB("COM4",115200);
	}
	
	public void txData(byte data[]) throws IOException
	{
		usbComm.sendData(data);
	}
	
	public byte[] rxData() throws IOException
	{		
		return usbComm.receiveData();
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
