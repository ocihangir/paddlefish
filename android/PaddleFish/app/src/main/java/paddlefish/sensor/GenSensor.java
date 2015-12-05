package paddlefish.sensor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import paddlefish.comm.I2CComm;
import paddlefish.comm.SPIComm;
import paddlefish.def.ControlInput;
import paddlefish.def.ControlInputValue;
import paddlefish.def.ControlType;
import paddlefish.def.SensorCategory;
import paddlefish.def.SensorControl;
import paddlefish.def.SensorIdent;
import paddlefish.def.SensorOutput;
import paddlefish.hal.CommControllerInterface;
import paddlefish.hal.CommReceiverInterface;
import paddlefish.io.SensorXMLReader;
import paddlefish.protocol.CommConstants;
import paddlefish.protocol.CommController;

public abstract class GenSensor implements CommReceiverInterface {
	// identification data read from XML file
	protected SensorIdent identInfo;
	// Null, if device does not support I2C
	protected I2CComm i2cInf;
	// Null, if device does not support SPI
	protected SPIComm spiInf;
	// Output Information of Sensor
	protected ArrayList<SensorOutput> outputLst;
	// Control Information of Sensor
	protected ArrayList<SensorControl> controlLst;
	// true if the sensor is up
	protected boolean isOpen;
	// ? we need a thread for this
	protected double curValue;
	/*
	 * Communication Variables
	 */
	// id for communication
	protected int commId;
	// waiting for a message to be received lock
	private final Object rcvlock = new Object();
	// received byte
	private byte[] rcvBuffer;
	
	protected CommController com;
	
	public int getCommId() {
		return commId;
	}

	public void setCommId(int commId) {
		this.commId = commId;
	}

	private void initFromXML(SensorCategory c, String devname) throws SAXException, IOException, ParserConfigurationException, URISyntaxException
	{
		SensorXMLReader reader = new SensorXMLReader(c, devname);
		reader.readFile();
		this.identInfo = reader.getIdentInfo();
		this.i2cInf = reader.getI2cInf();
		this.spiInf = reader.getSpiInf();
		this.outputLst = reader.getOutputLst();
		this.controlLst = reader.getControlLst();
	}
	
	public GenSensor(SensorCategory c, String devname) throws Exception
	{
		this.identInfo = new SensorIdent();
		this.outputLst = new ArrayList<SensorOutput>();
		this.controlLst = new ArrayList<SensorControl>();
		this.isOpen = false;
		this.curValue=0;
		this.commId = -1;
		this.initFromXML(c, devname);
		/* Communication */
		com = CommController.getInstance();
		com.addCommandReceiver(this);
		com.addDataReceiver(this);
	}
	
	public GenSensor() throws Exception
	{
		this.identInfo = new SensorIdent();
		this.outputLst = new ArrayList<SensorOutput>();
		this.controlLst = new ArrayList<SensorControl>();
		this.i2cInf = new I2CComm();
		this.spiInf = new SPIComm();
		this.isOpen = false;
		this.curValue=0;
		this.commId = -1;
		/* Communication */
		com = CommController.getInstance();
		com.addCommandReceiver(this);
		com.addDataReceiver(this);
	}
	
	public boolean getStatus()
	{
		return isOpen;
	}
	
	public I2CComm getI2cInf() {
		return i2cInf;
	}

	public SPIComm getSpiInf() {
		return spiInf;
	}

	public ArrayList<SensorOutput> getOutputLst() {
		return outputLst;
	}

	public ArrayList<SensorControl> getControlLst() {
		return controlLst;
	}

	public SensorIdent getIdentInfo() {
		return identInfo;
	}
	
	public boolean checkPhysicalValid() throws Exception
	{
		byte register = this.identInfo.deviceIDAddress;
		byte devId_gold = this.identInfo.deviceID;	
		// We set length as 1 ->> may be different check later
		com.readByteArray((byte)(this.getI2cInf().getActiveDeviceAddr()&0xff), register, 1, this.commId);
		// wait for the message to arrive
		synchronized(rcvlock) {
		    try {
		    	System.out.println("Waiting for receive lock");
		        // Calling wait() will block this thread until another thread calls notify() on the object.
		    	rcvlock.wait();
		    	System.out.println("Receive lock notified!");
		    	// check received buffer
		    	if(rcvBuffer.length>2)
		    	{
		    		System.out.println("Comparing device id with received buffer");
		    		byte devId_phy = this.rcvBuffer[3];
		    		// empty the received buffer
		    		return (devId_phy==devId_gold);
		    	}
		    } 
		    catch (InterruptedException e) {
		        // Happens if someone interrupts your thread.
		    }
		}
		return false;
	}
	
	public boolean updateOutputValues() throws Exception
	{
		int outputCount = this.outputLst.size();
		
		for ( int outputIndex = 0; outputIndex<outputCount; outputIndex++)
		{
			byte register = this.outputLst.get(outputIndex).register;
			int length = this.outputLst.get(outputIndex).length;
			
			com.readByteArray((byte)(this.getI2cInf().getActiveDeviceAddr()&0xff), register, length, this.commId);
			// wait for the message to arrive
			synchronized(rcvlock) {
			    try {
			    	System.out.println("Waiting for receive lock");
			        // Calling wait() will block this thread until another thread calls notify() on the object.
			    	rcvlock.wait();
			    	System.out.println("Receive lock notified!");
			    	// check received buffer
			    	int msgLength = this.rcvBuffer[1];
			    	if(rcvBuffer.length==msgLength+2)
			    	{
			    		byte[] outputData = new byte[msgLength-3];
			    		System.arraycopy(rcvBuffer, 3, outputData, 0, msgLength-3);
			    		this.outputLst.get(outputIndex).setOutputValue(outputData);
			    	} else {
			    		// TODO: log 
			    		System.out.println("The message length and length data mismatch!");
			    		return false;
			    	}
			    } 
			    catch (InterruptedException e) {
			        // Happens if someone interrupts your thread.
			    	return false;
			    }
			}
		}
		return true;
	}
	
	/*******
	 * Abstract Methods
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws Exception 
	 ******/
	// Power Down request from interface
	public abstract boolean powerDown() throws IOException, InterruptedException, Exception;
	// Power Up request from interface
	public abstract boolean powerUp() throws IOException, InterruptedException, Exception;
	// Read the value of the sensor continuously
	public abstract void getStreamData();
	//TODO: Imagine more...
	
	public void commCommandReceiver(byte[] buffer, Object receiver) 
	{
		System.out.println(" receiver: "+receiver+" my com id "+ this.commId);
		if((int)receiver==this.commId)
		{
			System.out.println("Command Received in GenSensor");
			synchronized(rcvlock) 
			{
				//TODO : Length check
				if(buffer!=null && buffer.length>=2)
				{
					System.out.println("Buffer is OK");
					// get command type
					byte cmdType = buffer[2];
					switch(cmdType)
					{
						case(CommConstants.CMD_READ_BYTES):
							System.out.println("Command Type is CMD_READ_BYTES");
							rcvBuffer = buffer;
							break;
						case(CommConstants.CMD_WRITE_BYTES):
							rcvBuffer = buffer;
							break;
						case(CommConstants.CMD_WRITE_BITS):
							rcvBuffer = buffer;
							break;
						default:
							break;
					}
					System.out.println("Notifying receive lock");
					rcvlock.notify();
				}			
			}
		}
		
	}
	
	public void commDataReceiver(byte[] buffer, Object receiver) 
	{
		System.out.println(" receiver: "+receiver+" my com id "+ this.commId);
		if((int)receiver==this.commId)
		{
			System.out.println("Command Received in GenSensor");
			synchronized(rcvlock) 
			{
				//TODO : Length check
				if(buffer!=null && buffer.length>=2)
				{
					System.out.println("Buffer is OK");
					// get command type
					byte cmdType = buffer[2];
					switch(cmdType)
					{
						case(CommConstants.CMD_READ_BYTES):
							System.out.println("Command Type is CMD_READ_BYTES");
							rcvBuffer = buffer;
							break;
						case(CommConstants.CMD_WRITE_BYTES):
							rcvBuffer = buffer;
							break;
						case(CommConstants.CMD_WRITE_BITS):
							rcvBuffer = buffer;
							break;
						default:
							break;
					}
					System.out.println("Notifying receive lock");
					rcvlock.notify();
				}			
			}
		}
	}

	public void testIdentificationInfo(SensorIdent ident)
	{
		System.out.println("IDENTIFICATION INFO");
		// Please note that all hexadecimal numbers are kept as decimals
		System.out.println("Device ID: "+ident.deviceID+" , DeviceName: "+ident.devName+" , Manufacturer: "+ident.manuf + " , Category: "+ident.categ.getFolderName());
	}
	
	public void testCommunicationInfo(I2CComm i2cc, SPIComm spic)
	{
		System.out.println("COMMUNICATION INFO");
		if(i2cc!=null)
		{
			System.out.println("Number of available addresses: "+i2cc.getDevAddrCnt()+"   , DeviceAddressInformation  "+i2cc.getDevAddrInf().toString());
		}
		else
		{
			System.out.println("This device does not support I2CComm");
		}
		if(spic!=null)
		{
			System.out.println("No idea about SPI ");
		}
		else
		{
			System.out.println("This device does not support SPIComm");
		}
	}
	
	public void testOutputInfo(ArrayList<SensorOutput> outputInfo)
	{
		System.out.println("OUTPUT INFO");
		for(SensorOutput sO:outputInfo)
		{
			System.out.println("Available Output:");
			System.out.println(sO.descr +" Type:  "+sO.dType+"  Length: "+sO.length+" Resolution: "+ sO.res+" bits"+" LSB: "+ sO.lsb+" Reg: "+ sO.register);
		}
	}
	
	public void testControlInfo(ArrayList<SensorControl> controlInfo)
	{
		System.out.println("CONTROL INFO");
		for(SensorControl sc:controlInfo)
		{
			System.out.println("Control Type: "+ sc.type.getDescr());
			for(ControlInput si:sc.cInputs)
			{
				System.out.println(si.name + "Interface Type: " +si.interfaceType+ " Number of Options: "+ si.noOptions + "  Default Option: "+ si.defOption+" register: "+ si.register);
				System.out.println("Options:");
				for(ControlInputValue civ:si.inValues)
				{
					System.out.println("Id: "+civ.id+" Value:  "+civ.value);
				}
			}
		}
	}

}
