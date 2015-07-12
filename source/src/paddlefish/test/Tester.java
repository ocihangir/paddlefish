package paddlefish.test;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import paddlefish.comm.I2CComm;
import paddlefish.comm.SPIComm;
import paddlefish.def.ControlInput;
import paddlefish.def.ControlInputValue;
import paddlefish.def.SensorCategory;
import paddlefish.def.SensorControl;
import paddlefish.def.SensorIdent;
import paddlefish.def.SensorOutput;
import paddlefish.sensor.ADXL345;

public class Tester {
	
	private void testIdentificationInfo(SensorIdent ident)
	{
		System.out.println("IDENTIFICATION INFO");
		// Please note that all hexadecimal numbers are kept as decimals
		System.out.println("Device ID: "+ident.deviceID+" , DeviceName: "+ident.devName+" , Manufacturer: "+ident.manuf + " , Category: "+ident.categ.getFolderName());
	}
	
	private void testCommunicationInfo(I2CComm i2cc, SPIComm spic)
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
	
	private void testOutputInfo(ArrayList<SensorOutput> outputInfo)
	{
		System.out.println("OUTPUT INFO");
		for(SensorOutput sO:outputInfo)
		{
			System.out.println("Available Output:");
			System.out.println(sO.descr +" Type:  "+sO.dType+"  Length: "+sO.length+" Resolution: "+ sO.res+" bits");
		}
	}
	
	private void testControlInfo(ArrayList<SensorControl> controlInfo)
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
	
	public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
		// Assume we have added ADXL345
		ADXL345 adSens = new ADXL345(SensorCategory.ACC, "ADXL345");
		
		Tester test = new Tester();
		test.testIdentificationInfo(adSens.getIdentInfo());
		test.testCommunicationInfo(adSens.getI2cInf(), adSens.getSpiInf());
		test.testOutputInfo(adSens.getOutputLst());
		test.testControlInfo(adSens.getControlLst());
	}

}
