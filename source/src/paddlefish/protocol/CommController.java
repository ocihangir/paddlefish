package paddlefish.protocol;

import java.io.IOException;

import paddlefish.hal.HAL;
import paddlefish.protocol.CommConstants;


/*Singleton class Pattern is used*/
public class CommController 
{
	private static CommController instance = null;
	private static HAL hal;
	
	protected CommController() throws Exception 
	{
		// Exists only to defeat instantiation.
		//http://www.javaworld.com/article/2073352/core-java/simply-singleton.html
		 if(hal==null)
			hal = new HAL();
	}
	   

    public static CommController getInstance() throws Exception {
	      if(instance == null) 
	      {
	         instance = new CommController();
	      }
	      return instance;
	}
	   
	public byte[] readByteArray(byte deviceAddress, byte registerAddress, int length) throws IOException, InterruptedException
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
		
		Thread.sleep(50);
		byte[] receivedData = hal.rxData();
		
		return receivedData;
	}
	
	public void writeSingleByte(byte deviceAddress, byte registerAddress, byte data) throws IOException
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
	}
	
	public void writeByteArray(byte deviceAddress, byte registerAddress, int length, byte data[]) throws IOException
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
	}
	
	public void writeBits(byte deviceAddress, byte registerAddress, byte data, byte mask) throws IOException, InterruptedException
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
		
		Thread.sleep(50);
		byte[] receivedData = hal.rxData();
		
		//return receivedData;
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
}
