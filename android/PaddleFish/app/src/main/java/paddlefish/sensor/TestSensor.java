package paddlefish.sensor;

import java.util.ArrayList;

import paddlefish.def.ControlInput;
import paddlefish.def.ControlInputValue;
import paddlefish.def.ControlType;
import paddlefish.def.SensorCategory;
import paddlefish.def.SensorControl;
import paddlefish.def.SensorOutput;
import paddlefish.protocol.CommController;

public class TestSensor extends GenSensor {
	
	CommController com;

	public TestSensor() throws Exception {
		super();
		this.identInfo.deviceID = 0x01;
		this.identInfo.devName = "TestSensor";
		this.identInfo.manuf="ADI";
		this.identInfo.categ = SensorCategory.GYRO;
		this.i2cInf.addDeviceAddr((byte)0x53, true);
		this.i2cInf.addDeviceAddr((byte)0x3A, false);
		this.spiInf = null;
		SensorOutput o1 = new SensorOutput();
		o1.descr = "x axis";
		o1.dType = "byte";
		o1.length= 2;
		o1.res = 13;
		o1.register = 0x32;
		this.outputLst.add(o1);
		SensorOutput o2 = new SensorOutput();
		o2.descr = "y axis";
		o2.dType = "byte";
		o2.length= 2;
		o2.res = 13;
		o2.register = 0x34;
		this.outputLst.add(o2);
		SensorOutput o3 = new SensorOutput();
		o3.descr = "z axis";
		o3.dType = "byte";
		o3.length= 2;
		o3.res = 13;
		o3.register = 0x36;
		this.outputLst.add(o3);
		/*Sensor Control 1*/
		SensorControl c1 = new SensorControl();
		c1.descr = "Power Down";
		c1.type = ControlType.POWERDOWN;
		ControlInput ci = new ControlInput();
		ci.bit = 1;
		ci.defOption=0;
		ci.interfaceType="checkbox";
		ci.name="Power Down";
		ci.noOptions=1;
		ci.register=0x2D;
		ci.inValues = new ArrayList<ControlInputValue>();
        ControlInputValue civ = new ControlInputValue();
        civ.id=0;
        civ.value=false;
        ci.inValues.add(civ);
        c1.cInputs.add(ci);
		this.controlLst.add(c1);
		/*Sensor Control 2*/
		SensorControl c2 = new SensorControl();
		c2.descr = "Measure Activity";
		c2.type = ControlType.MEASURE;
		ControlInput c2i = new ControlInput();
		c2i.bit = 1;
		c2i.defOption=0;
		c2i.interfaceType="checkbox";
		c2i.name="measure";
		c2i.noOptions=1;
		c2i.register=0x2D;
		c2i.inValues = new ArrayList<ControlInputValue>();
        ControlInputValue c2iv = new ControlInputValue();
        c2iv.id=0;
        c2iv.value=true;
        c2i.inValues.add(c2iv);
        c2.cInputs.add(c2i);
		this.controlLst.add(c2);
		/*Sensor Control 3*/
		SensorControl c3 = new SensorControl();
		c3.descr = "Go to sleep";
		c2.type = ControlType.SLEEP;
		ControlInput c3i = new ControlInput();
		c3i.bit = 1;
		c3i.defOption=0;
		c3i.interfaceType="checkbox";
		c3i.name="sleep";
		c3i.noOptions=1;
		c3i.register=0x2D;
		c3i.inValues = new ArrayList<ControlInputValue>();
        ControlInputValue c3iv = new ControlInputValue();
        c3iv.id=0;
        c3iv.value=false;
        c3i.inValues.add(c3iv);
        c3.cInputs.add(c3i);
		this.controlLst.add(c3);
		/*Sensor Control 4*/
		SensorControl c4 = new SensorControl();
		c4.descr = "Wake up the device";
		c4.type = ControlType.WAKEUP;
		ControlInput c4i = new ControlInput();
		c4i.bit = 1;
		c4i.defOption=0;
		c4i.interfaceType="combo";
		c4i.name="Frequency";
		c4i.noOptions=4;
		c4i.register=0x2D;
		c4i.inValues = new ArrayList<ControlInputValue>();
		
        ControlInputValue c4iv = new ControlInputValue();
        c4iv.id=0;
        c4iv.value=8;
        c4i.inValues.add(c4iv);
        
        ControlInputValue c4iv2 = new ControlInputValue();
        c4iv2.id=1;
        c4iv2.value=4;
        c4i.inValues.add(c4iv2);
        
        ControlInputValue c4iv3 = new ControlInputValue();
        c4iv3.id=2;
        c4iv3.value=2;
        c4i.inValues.add(c4iv3);
        
        ControlInputValue c4iv4 = new ControlInputValue();
        c4iv4.id=3;
        c4iv4.value=1;
        c4i.inValues.add(c4iv4);
        c4.cInputs.add(c4i);
		this.controlLst.add(c4);

	}

	@Override
	public boolean powerDown() throws Exception {
		// TODO Protokol gelince yazılacak
		// If power down is successful
		if (this.isOpen) {
			byte register = 0x00;
			int bit = 0;
			byte mask = 0x01;
			byte value = 0x00;
			for (SensorControl cnt : this.getControlLst()) {
				if (cnt.type == ControlType.POWERDOWN) {
					register = cnt.cInputs.get(0).register;
					bit = cnt.cInputs.get(0).bit;
					mask = (byte) (mask << bit);
				}
			}

			com.writeBits(
					(byte) (this.getI2cInf().getActiveDeviceAddr() & 0xff),
					register, value, mask);
			this.isOpen = false;
		}
		return false;
	}

	@Override
	public boolean powerUp() throws Exception {
		// TODO Protokol gelince yazılacak
		// If power up is successful
		if (!this.isOpen) {
			byte register = 0x00;
			int bit = 0;
			byte mask = 0x01;
			byte value = (byte) 0xFF;
			for (SensorControl cnt : this.getControlLst()) {
				if (cnt.type == ControlType.MEASURE) {
					register = cnt.cInputs.get(0).register;
					bit = cnt.cInputs.get(0).bit;
					mask = (byte) (mask << bit);
				}
			}

			com.writeBits(
					(byte) (this.getI2cInf().getActiveDeviceAddr() & 0xff),
					register, value, mask);
			this.isOpen = true;
		}
		return false;
	}

	@Override
	public void getStreamData() 
	{
	
	}

	public static void main(String[] args) throws Exception 
	{
		TestSensor adSens = new TestSensor();
		adSens.testIdentificationInfo(adSens.getIdentInfo());
		adSens.testCommunicationInfo(adSens.getI2cInf(), adSens.getSpiInf());
		adSens.testOutputInfo(adSens.getOutputLst());
		adSens.testControlInfo(adSens.getControlLst());
	}
}
