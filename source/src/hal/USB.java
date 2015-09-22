package paddlefish.hal;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

// TODO : implement an interface for serial communication
public class USB implements SerialPortEventListener
{
	InputStream in;
	OutputStream out;

	SerialPort commPort;
	SerialPortList serialPorts;	
	
	private List<CommRxInterface> receiverList = new ArrayList<CommRxInterface>();
	
	public USB() throws Exception
	{	
		
	}
	
	public void addReceiver(CommRxInterface commRx)
    {
    	receiverList.add(commRx);
    }
	
	public boolean isConnected()
	{
		if ( commPort == null)
			return false;
		
		return commPort.isOpened();
	}
	
	/*
	 * Connect to the serial port and hold it
	 */
	void connect( String portName, int baudRate ) throws SerialPortException 
	{
		commPort = new SerialPort(portName);
		commPort.openPort();
		commPort.setParams(baudRate, 8, 1, 0);
		commPort.addEventListener(this);
	}
	
	public void disconnect() throws SerialPortException
	{
		commPort.closePort();
	}
	
	public void close() throws SerialPortException
	{
		if(this.commPort!=null)
			commPort.closePort();
		//TODO: Log
		else
			System.out.println("No CommPort available");
	}
	
	public void sendData(byte[] data) throws SerialPortException
	{
		byte buffer[] = new byte[data.length];
		for (int i=0;i<data.length;i++)
			buffer[i] = (byte) data[i];
		commPort.writeBytes(buffer);
	}
	
	public byte[] receiveData(int timeout) throws Exception
	{
		int len = commPort.getInputBufferBytesCount();
		byte[] buffer = commPort.readBytes(len,timeout);
			
		return buffer;
	}
	
	public void serialEvent(SerialPortEvent event) {
		if(event.isRXCHAR()){//If data is available
			if(event.getEventValue() > 0){//Check bytes count in the input buffer
				//Read data, if 10 bytes available 
				try {
					byte buffer[] = commPort.readBytes();
					for (CommRxInterface commRx : receiverList)
			        	commRx.commReceiver(buffer);
				}
				catch (SerialPortException ex) {
					System.out.println(ex);
				}
			}
		}           
	}

	
	  public ArrayList<String> listPorts()
	    {
		  
		  ArrayList<String> availablePorts = new ArrayList<String>();
		  
		  String[] ports = SerialPortList.getPortNames();
		  
		  for (int i = 0;i<ports.length; i++)
			  availablePorts.add(ports[i]);
		  
	        return availablePorts;
	    }
}
