package paddlefish.sensor;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import paddlefish.def.SensorCategory;

public class ADXL345 extends GenSensor{
	private static final int AXIS = 3;
	
	public ADXL345(SensorCategory c, String devname) throws SAXException,
			IOException, ParserConfigurationException {
		super(c, devname);
		// TODO Auto-generated constructor stub
		this.startMeasuring();
	}
	
	// Specific to this sensor
	
	private void startMeasuring()
	{
		// Should activate the control measure!
	}
	
	@Override
	public boolean powerDown() {
		// TODO Protokol gelince yazılacak
		// If power down is successful
		if(this.isOpen) this.isOpen=false;
		return false;
	}

	@Override
	public boolean powerUp() {
		// TODO Protokol gelince yazılacak
		// If power up is successful
		if(!this.isOpen) this.isOpen=true;
		return false;
	}

	@Override
	public void getStreamData() {
		// TODO Protokol gelince yazılacak
		for(int i=0;i<AXIS;i++)
		{
			// Do something, don't know it yet
		}
	}

	
}
