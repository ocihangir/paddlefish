package paddlefish.sensor;

import paddlefish.def.ControlType;
import paddlefish.def.SensorCategory;
import paddlefish.def.SensorControl;
import paddlefish.protocol.CommController;

public class ADXL345 extends GenSensor {
	private static final int AXIS = 3;
	CommController com;

	public ADXL345() throws Exception {
		super();
		// TODO Auto-generated constructor stub
		com = CommController.getInstance();
		this.startMeasuring();
	}

	// Specific to this sensor

	private void startMeasuring() {
		// Should activate the control measure!
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
	public void getStreamData() {
		// TODO Protokol gelince yazılacak
		for (int i = 0; i < AXIS; i++) {
			// Do something, don't know it yet
		}
	}

	public static void main(String[] args) throws Exception 
	{
		ADXL345 adSens = new ADXL345();
		adSens.testIdentificationInfo(adSens.getIdentInfo());
		adSens.testCommunicationInfo(adSens.getI2cInf(), adSens.getSpiInf());
		adSens.testOutputInfo(adSens.getOutputLst());
		adSens.testControlInfo(adSens.getControlLst());
	}
}