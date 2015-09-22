package paddlefish.protocol;

import java.util.ArrayList;
import java.util.List;
import paddlefish.hal.CommStreamerInterface;
import paddlefish.hal.HAL;

public class CommStreamer implements CommStreamerInterface {

	private static HAL hal;
	private List<CommStreamerInterface> streamReceiverList = new ArrayList<CommStreamerInterface>();
		
	public CommStreamer() throws Exception 
	{
		 if(hal==null)
			hal = HAL.getInstance();
		 hal.addStreamReceiver(this);
	}
	
	public void addStreamReceiver(CommStreamerInterface commRx)
    {
		streamReceiverList.add(commRx);
    }
	
	public void addDevice(byte deviceAddress, byte registerAddress, int length, int period) throws Exception
	{
		addDevice(deviceAddress, registerAddress, length, period, 0);
	}
	
	public void addDevice(byte deviceAddress, byte registerAddress, int length, int period, int sender) throws Exception
	{
		byte cmd[] = new byte[9];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_STREAM_ADD;
		cmd[2] = deviceAddress;
		cmd[3] = registerAddress;
		cmd[4] = (byte) length;
		cmd[5] = (byte)( period & 0xFF );
		cmd[6] = (byte)( ( period >> 8 ) & 0xFF );
		cmd[7] = 0x00;
		cmd[8] = CommConstants.CMD_END;
		
		hal.txData(cmd);
	}
	
	public void setPeriod(int period) throws Exception
	{
		setPeriod(period, 0);
	}
	
	public void setPeriod(int period, int sender) throws Exception
	{
		byte cmd[] = new byte[6];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_STREAM_PERIOD;
		cmd[2] = (byte)( ( period >> 8 ) & 0xFF );
		cmd[3] = (byte)( period & 0xFF );
		cmd[4] = 0x00;
		cmd[5] = CommConstants.CMD_END;
		
		hal.txData(cmd);
	}
	
	public void start() throws Exception
	{
		start(0);
	}
	
	public void start(int sender) throws Exception
	{
		byte cmd[] = new byte[5];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_STREAM_ON;
		cmd[2] = 0x01;
		cmd[3] = 0x00;
		cmd[4] = CommConstants.CMD_END;
		
		hal.txData(cmd);
	}
	
	public void stop() throws Exception
	{
		stop(0);
	}
	
	public void stop(int sender) throws Exception
	{
		byte cmd[] = new byte[5];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_STREAM_ON;
		cmd[2] = 0x00;
		cmd[3] = 0x00;
		cmd[4] = CommConstants.CMD_END;
		
		hal.txData(cmd);
	}
	
	public void reset() throws Exception
	{
		reset(0);
	}
	
	public void reset(int sender) throws Exception
	{
		byte cmd[] = new byte[4];
		
		cmd[0] = CommConstants.CMD_START;
		cmd[1] = CommConstants.CMD_STREAM_RST;
		cmd[2] = 0x00;
		cmd[3] = CommConstants.CMD_END;
		
		hal.txData(cmd);
	}

	@Override
	public void streamReceiver(byte[] buffer) {
		for (CommStreamerInterface commStreamer : streamReceiverList)
			commStreamer.streamReceiver(buffer);
	}
	
}
