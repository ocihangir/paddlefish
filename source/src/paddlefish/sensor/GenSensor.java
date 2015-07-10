package paddlefish.sensor;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import paddlefish.comm.I2CComm;
import paddlefish.comm.SPIComm;
import paddlefish.def.SensorCategory;
import paddlefish.def.SensorControl;
import paddlefish.def.SensorIdent;
import paddlefish.def.SensorOutput;
import paddlefish.io.SensorXMLReader;

public abstract class GenSensor {
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
	
	private void initFromXML(SensorCategory c, String devname) throws SAXException, IOException, ParserConfigurationException
	{
		SensorXMLReader reader = new SensorXMLReader(c, devname);
		reader.readFile();
		this.identInfo = reader.getIdentInfo();
		this.i2cInf = reader.getI2cInf();
		this.spiInf = reader.getSpiInf();
		this.outputLst = reader.getOutputLst();
		this.controlLst = reader.getControlLst();
	}
	
	public GenSensor(SensorCategory c, String devname) throws SAXException, IOException, ParserConfigurationException
	{
		this.identInfo = new SensorIdent();
		this.outputLst = new ArrayList<SensorOutput>();
		this.controlLst = new ArrayList<SensorControl>();
		this.isOpen = false;
		this.curValue=0;
		this.initFromXML(c, devname);
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
	
	/*******
	 * Abstract Methods
	 ******/
	// Power Down request from interface
	public abstract boolean powerDown();
	// Power Up request from interface
	public abstract boolean powerUp();
	// Read the value of the sensor continuously
	public abstract void getStreamData();
	//TODO: Imagine more...
}
