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
		try {
			CommStreamer streamer = new CommStreamer();
			byte devAddress = (byte)0x53; // ADXL345
			byte regAddress = (byte)0x00;
			byte length = 1;
			int period = 0;
			streamer.addDevice(devAddress, regAddress, length, period);
			//assertTrue("addDevice must return true", streamer.addDevice(devAddress, regAddress, length, period));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void addUnknownDeviceTest()
	{
		try {
			CommStreamer streamer = new CommStreamer();
			byte devAddress = (byte)0xFE; // Unknown device 
			byte regAddress = (byte)0x00;
			byte length = 1;
			int period = 0;
			streamer.addDevice(devAddress, regAddress, length, period);
			//assertTrue("addDevice must return true", streamer.addDevice(devAddress, regAddress, length, period));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void setPeriodTest()
	{
		try {
			CommStreamer streamer = new CommStreamer();
			int period = 1000; //ms
			streamer.setPeriod(period);
			//assertTrue("setPeriod must return true", streamer.setPeriod(period));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void setPeriodToOutOfRangeTest()
	{
		try {
			CommStreamer streamer = new CommStreamer();
			int period = 10000000; //ms
			streamer.setPeriod(period);
			//assertFalse("setPeriod to out of range value must return false", streamer.setPeriod(period));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void setPeriodToNegativeValueTest()
	{
		try {
			CommStreamer streamer = new CommStreamer();
			int period = -1000; //ms
			streamer.setPeriod(period);
			//assertFalse("setPeriod to negative value must return false", streamer.setPeriod(period));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void startStreamTest()
	{
		try {
			CommStreamer streamer = new CommStreamer();
			streamer.start();
			//assertTrue("start must return true", streamer.start());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void stopStreamTest()
	{
		try {
			CommStreamer streamer = new CommStreamer();
			streamer.stop();
			//assertTrue("stop must return true", streamer.stop());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void resetStreamQueueTest()
	{
		try {
			CommStreamer streamer = new CommStreamer();
			streamer.reset();
			//assertTrue("reset must return true", streamer.reset());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
