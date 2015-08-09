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
		
		byte[] data = usbComm.receiveData();
		byte[] buffer = new byte[data.length];
		for (int i=0;i<data.length;i++)
			buffer[i] = (byte) data[i];
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
