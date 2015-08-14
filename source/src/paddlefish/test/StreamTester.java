package paddlefish.test;

import static org.junit.Assert.*;

import org.junit.Test;

import paddlefish.protocol.CommStreamer;

public class StreamTester {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
		
	@Test
	public void addKnownDeviceTest()
	{
		CommStreamer streamer = new CommStreamer();
		byte devAddress = (byte)0x53; // ADXL345
		byte regAddress = (byte)0x00;
		byte length = 1;
		int period = 0;
		assertTrue("addDevice must return true", streamer.addDevice(devAddress, regAddress, length, period));
	}
	
	@Test
	public void addUnknownDeviceTest()
	{
		CommStreamer streamer = new CommStreamer();
		byte devAddress = (byte)0xFE; // Unknown device 
		byte regAddress = (byte)0x00;
		byte length = 1;
		int period = 0;
		assertTrue("addDevice must return true", streamer.addDevice(devAddress, regAddress, length, period));
	}
	
	@Test
	public void setPeriodTest()
	{
		CommStreamer streamer = new CommStreamer();
		int period = 1000; //ms
		assertTrue("setPeriod must return true", streamer.setPeriod(period));
	}
	
	@Test
	public void setPeriodToOutOfRangeTest()
	{
		CommStreamer streamer = new CommStreamer();
		int period = 10000000; //ms
		assertFalse("setPeriod to out of range value must return false", streamer.setPeriod(period));
	}
	
	@Test
	public void setPeriodToNegativeValueTest()
	{
		CommStreamer streamer = new CommStreamer();
		int period = -1000; //ms
		assertFalse("setPeriod to negative value must return false", streamer.setPeriod(period));
	}
	
	@Test
	public void startStreamTest()
	{
		CommStreamer streamer = new CommStreamer();
		assertTrue("start must return true", streamer.start());
	}
	
	@Test
	public void stopStreamTest()
	{
		CommStreamer streamer = new CommStreamer();
		assertTrue("stop must return true", streamer.stop());
	}
	
	@Test
	public void resetStreamQueueTest()
	{
		CommStreamer streamer = new CommStreamer();
		assertTrue("reset must return true", streamer.reset());
	}

}
