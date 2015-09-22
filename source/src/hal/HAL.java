package paddlefish.hal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import jssc.SerialPortException;

import paddlefish.protocol.CommConstants;


/*Singleton class Pattern is used*/
/*Observer Pattern is used*/
public class HAL implements CommRxInterface {
	private static HAL instance = null;
	USB usbComm;
	private List<CommControllerInterface> dataReceiverList = new ArrayList<CommControllerInterface>();
	private List<CommControllerInterface> commandReceiverList = new ArrayList<CommControllerInterface>();
	private List<CommStreamerInterface> streamReceiverList = new ArrayList<CommStreamerInterface>();
	private byte rxBuffer[] = new byte[1024];
	private int rxBufferLength = 0;
	Mutex mutex = new Mutex();
	
	protected HAL() throws Exception
	{
		if (usbComm == null)
		{
			usbComm = new USB();
			usbComm.addReceiver(this);
		}
	}
	
	public static HAL getInstance() throws Exception {
	      if(instance == null) 
	      {
	         instance = new HAL();
	      }
	      return instance;
	}
	
	public void addDataReceiver(CommControllerInterface commRx)
    {
		dataReceiverList.add(commRx);
    }
	
	public void addCommandReceiver(CommControllerInterface commRx)
    {
		commandReceiverList.add(commRx);
    }
	
	public void addStreamReceiver(CommStreamerInterface commRx)
    {
		streamReceiverList.add(commRx);
    }
	
	public ArrayList<String> listAvailablePorts()
	{
		return usbComm.listPorts();
	}
	
	public void connect(String port, int baud) throws SerialPortException
	{
		usbComm.connect(port,baud);
	}
	
	public void disconnect() throws SerialPortException
	{
		usbComm.disconnect();
	}
	
	public boolean isConnected()
	{
		return usbComm.isConnected();
	}
	
	public void txData(byte data[]) throws IOException
	{
		
			try {
				mutex.acquire();
				try {
					try {
					usbComm.sendData(data);
					} catch (SerialPortException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} finally {
					mutex.release();
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
	}
	
	public byte[] rxData() throws Exception
	{		
		byte[] resBuffer = new byte[1024];
		int len = 0;
		int prev_len = 0;
		boolean loop = true;
		do 
		{
			byte[] buffer = usbComm.receiveData(2000);
			len = buffer.length;
			
			System.arraycopy(buffer, 0, resBuffer, prev_len, len);
			prev_len+=len;
			
			if (prev_len>0)
			{
				if (resBuffer[prev_len-1] == 0x0C)
				{
					loop = false;
				}
				
				if (resBuffer[prev_len-1] == 0x0E)
				{
					loop = false;
					throw new Exception("I2C Error! Check if I2C device connected properly. Slow down the I2C speed from Advanced tab.");
					// TODO : Create an exception class for I2C
				}
			}
		} while( loop );
		byte[] res = new byte[prev_len];
		System.arraycopy(resBuffer, 0, res, 0, prev_len);
		return res;
	}

	public void close()
	{
		if(this.usbComm!=null)
			try {
				this.usbComm.close();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			System.out.println("No UsbComm available");
	}

	@Override
	public void commReceiver(byte[] buffer) {
		int len = 0;
		byte tempBuffer[] = null;
		
		len = buffer.length;
		System.arraycopy(buffer, 0, rxBuffer, rxBufferLength, len);
		rxBufferLength+=len;
		do //if (len>0)
		{
			
			
			int lastPos=0;
			for (int i = 0; i < rxBufferLength;i++)
			{
				if (((byte)rxBuffer[i] == (byte)CommConstants.CMD_ANSWER) || ((byte)rxBuffer[i] == (byte)CommConstants.CMD_DATA_ANSWER) || ((byte)rxBuffer[i] == (byte)CommConstants.CMD_STREAM_START))
				{
					// Start character detected
					if (rxBufferLength>i+1) // Check length exists
					{
						// Check there are enough characters in the buffer
						if (rxBufferLength>=i+1+(int)(rxBuffer[i+1]&0xFF))
						{
							lastPos = i+1+(int)(rxBuffer[i+1]&0xFF);
							// Check if last character is known
							if (((byte)rxBuffer[lastPos] == (byte)CommConstants.CMD_ESC) || ((byte)rxBuffer[lastPos] == (byte)CommConstants.CMD_END) || ((byte)rxBuffer[lastPos] == (byte)CommConstants.CMD_STREAM_END))  
							{
								tempBuffer = new byte[lastPos-i+1];
								System.arraycopy(rxBuffer, i, tempBuffer, 0, lastPos-i+1);
								break;
							}
						}
					}
				}
				
			}
			
			if (tempBuffer != null)
			{
				// TODO : check CRC here
				
				if ((byte)tempBuffer[0] == (byte)CommConstants.CMD_ANSWER)
				{
					if ((byte)tempBuffer[tempBuffer.length-1] == (byte)CommConstants.CMD_END)
					{
						for (CommControllerInterface commRx : commandReceiverList)
				        	commRx.commCommandReceiver(tempBuffer);
					}
					
				} else if ((byte)tempBuffer[0] == (byte)CommConstants.CMD_DATA_ANSWER)
				{
					if ((byte)tempBuffer[tempBuffer.length-1] == (byte)CommConstants.CMD_END)
					{
						for (CommControllerInterface commRx : dataReceiverList)
				        	commRx.commDataReceiver(tempBuffer);
					}
				} else if  ((byte)tempBuffer[0] == (byte)CommConstants.CMD_STREAM_START)
				{
					if ((byte)tempBuffer[tempBuffer.length-1] == (byte)CommConstants.CMD_STREAM_END)
					{
						for (CommStreamerInterface commRx : streamReceiverList)
				        	commRx.streamReceiver(tempBuffer);
					}
				}
				
				// Refresh array with remaining data
				if (rxBufferLength<=lastPos)
					rxBufferLength=0;
				else
				{
					int remainingCount = rxBufferLength-(lastPos+1);
					System.arraycopy(rxBuffer, lastPos+1, rxBuffer, 0, remainingCount);
					rxBufferLength = remainingCount;
					tempBuffer = null;
				}
			} else {
				break;
			}
		} while(rxBufferLength>0);
	}
}
