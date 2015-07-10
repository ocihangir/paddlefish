package paddlefish.protocol;

import java.io.IOException;

import paddlefish.hal.HAL;
import paddlefish.protocol.CommConstants;

public class CommController {
	HAL hal;
	public CommController() throws Exception
	{
		 hal = new HAL();
	}
	
	public char[] readByteArray(char deviceAddress, char registerAddress, int length) throws IOException, InterruptedException
	{
		char cmd[] = new char[7];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_READ_BYTES;
		cmd[2] = deviceAddress;
		cmd[3] = registerAddress;
		cmd[4] = (char) length;
		cmd[5] = 0x00;
		cmd[6] = CommConstants.CMD_END;
		
		hal.txData(cmd);
		
		Thread.sleep(50);
		char[] receivedData = hal.rxData();
		
		return receivedData;
	}
	
	public void writeSingleByte(char deviceAddress, char registerAddress, char data)
	{
		
	}
	
	public void writeByteArray(char deviceAddress, char registerAddress, int length, char data[])
	{
		
	}
	
	public void writeBits(char deviceAddress, char registerAddress, char data, char mask)
	{
		
	}
}
