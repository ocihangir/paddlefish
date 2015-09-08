package paddlefish.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import jssc.SerialPortException;
import paddlefish.hal.CommControllerInterface;
import paddlefish.hal.CommReceiverInterface;
import paddlefish.hal.HAL;
import paddlefish.protocol.CommConstants;



/*Observer Pattern is used*/
public class CommController implements CommControllerInterface
{
	private static CommController instance = null;
	private static HAL hal;
	
	private List<CommReceiverInterface> dataReceiverList = new ArrayList<CommReceiverInterface>();
	private List<CommReceiverInterface> commandReceiverList = new ArrayList<CommReceiverInterface>();
	
	Queue<Object> sendQueue = new LinkedBlockingQueue<>();
	
	protected CommController() throws Exception 
	{
		 if(hal==null)
		 {
			hal = HAL.getInstance();
			hal.addDataReceiver(this);
			hal.addCommandReceiver(this);
		 }
	}
	
	public static CommController getInstance() throws Exception
	{
		if(instance == null) 
	      {
	         instance = new CommController();
	      }
	      return instance;
	}
    
    public void addDataReceiver(CommReceiverInterface commRx)
    {
    	dataReceiverList.add(commRx);
    }
    
    public void addCommandReceiver(CommReceiverInterface commRx)
    {
    	commandReceiverList.add(commRx);
    }
    
    public boolean isConnected()
    {
    	return hal.isConnected();
    }
    
    public ArrayList<String> listPorts()
    {
    	ArrayList<String> ports = hal.listAvailablePorts(); 
    	return ports;
    }
    
    public void connect(String port, int baud) throws SerialPortException
    {
    	hal.connect(port, baud);    	
    }
	   
    public void disconnect() throws SerialPortException
    {
    	hal.disconnect();
    }
    
    public void readByteArray(byte deviceAddress, byte registerAddress, int length) throws Exception
    {
    	readByteArray(deviceAddress, registerAddress, length, 0);
    }
    
	public void readByteArray(byte deviceAddress, byte registerAddress, int length, int sender) throws Exception
	{
		byte cmd[] = new byte[7];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_READ_BYTES;
		cmd[2] = deviceAddress;
		cmd[3] = registerAddress;
		cmd[4] = (byte) length;
		cmd[5] = 0x00;
		cmd[6] = CommConstants.CMD_END;
		
		hal.txData(cmd);
		
		sendQueue.add(sender);
	}
	
	public void writeSingleByte(byte deviceAddress, byte registerAddress, byte data) throws Exception
	{
		writeSingleByte(deviceAddress, registerAddress, data, 0);
	}
	
	public void writeSingleByte(byte deviceAddress, byte registerAddress, byte data, int sender) throws Exception
	{
		byte cmd[] = new byte[8];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_WRITE_BYTES;
		cmd[2] = deviceAddress;
		cmd[3] = registerAddress;
		cmd[4] = 0x01;
		cmd[5] = CommConstants.CMD_END;
		cmd[6] = data;
		cmd[7] = CommConstants.CMD_END;
		
		hal.txData(cmd);
		
		sendQueue.add(sender);
	}
	
	public void writeByteArray(byte deviceAddress, byte registerAddress, int length, byte data[]) throws Exception
	{
		writeByteArray(deviceAddress, registerAddress, length, data, 0);
	}
	
	public void writeByteArray(byte deviceAddress, byte registerAddress, int length, byte data[], int sender) throws Exception
	{
		byte cmd[] = new byte[8+length];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_WRITE_BYTES;
		cmd[2] = deviceAddress;
		cmd[3] = registerAddress;
		cmd[4] = (byte)length;
		cmd[5] = CommConstants.CMD_END;		
		cmd[6+length] = CommConstants.CMD_END;
		
		System.arraycopy(data, 0, cmd, 6, length);
		
		hal.txData(cmd);
		
		sendQueue.add(sender);
	}
	
	public void writeBits(byte deviceAddress, byte registerAddress, byte data, byte mask) throws Exception
	{
		writeBits(deviceAddress, registerAddress, data, mask, 0);
	}
	
	public void writeBits(byte deviceAddress, byte registerAddress, byte data, byte mask, int sender) throws Exception
	{
		byte cmd[] = new byte[8];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_WRITE_BITS;
		cmd[2] = deviceAddress;
		cmd[3] = registerAddress;
		cmd[4] = data;
		cmd[5] = mask;
		cmd[6] = 0x00;
		cmd[7] = CommConstants.CMD_END;
		
		hal.txData(cmd);
		
		sendQueue.add(sender);
	}
	
	public void setI2CSpeed(long speed) throws Exception
	{
		setI2CSpeed(speed, 0);
	}
	
	public void setI2CSpeed(long speed, int sender) throws Exception
	{
		byte cmd[] = new byte[8];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_SET_I2C_SPEED;
		cmd[2] = (byte)((speed>>24) & 0xFF);
		cmd[3] = (byte)((speed>>16) & 0xFF);
		cmd[4] = (byte)((speed>>8) & 0xFF);
		cmd[5] = (byte)((speed>>0) & 0xFF);
		cmd[6] = 0x00;
		cmd[7] = CommConstants.CMD_END;
		
		hal.txData(cmd);
		
		sendQueue.add(sender);
	}
	
	public void testData(byte length) throws Exception
	{
		testData(length, 0);
	}
	
	public void testData(byte length, int sender) throws Exception
	{
		byte cmd[] = new byte[5];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_DATA_TEST;
		cmd[2] = length;
		cmd[3] = 0x00;
		cmd[4] = CommConstants.CMD_END;
		
		hal.txData(cmd);
		
		sendQueue.add(sender);
	}
	
	public void close()
	{
		if(hal!=null)
		{
			hal.close();
		}
		//TODO: Log
		else
			System.out.println("No HAL available");
	}
	
	@Override
	public void commCommandReceiver(byte[] receivedMessage) {
		for (CommReceiverInterface commRx : commandReceiverList)
        	commRx.commCommandReceiver(receivedMessage, sendQueue.poll());
	}


	@Override
	public void commDataReceiver(byte[] receivedMessage) {
		for (CommReceiverInterface commRx : dataReceiverList)
        	commRx.commDataReceiver(receivedMessage, sendQueue.poll());
	}
}
