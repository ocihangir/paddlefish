package paddlefish.run;

import java.util.ArrayList;

import paddlefish.def.SensorCategory;
import paddlefish.sensor.*;

public class State 
{
	// Keeps currently active sensors in the list
	private ArrayList<GenSensor> activeSensLst;
	// number of sensors currently in the system
	private int sensCnt;
	// Return active sensor list
	
	public State()
	{
		sensCnt = 0;
		activeSensLst = new ArrayList<GenSensor>();
	}
	
	public ArrayList<GenSensor> getActiveSensLst()
	{
		return this.activeSensLst;
	}
	
	/*
	 * devname: Available codable devices with XML files
	 * return newSensor: new Sensor added to the system
	 */
	public GenSensor addDevice(String devname) throws Exception
	{
		GenSensor newSensor = null;
		switch(devname)
		{
		case "ADXL345":
			newSensor = new ADXL345();
			break;
		case "L3GD20":
			newSensor = new L3GD20();
			break;
		default:
			//TODO: Log!
			System.out.println("Devname does not exist");
			break;
		}
		if(newSensor!=null)
		{   
			// assign a commId (important to send & recieve messages from queue)
			newSensor.setCommId(sensCnt++);
			// check if it is valid
			if(newSensor.checkPhysicalValid())
			{
				// add it to active sensor list
				activeSensLst.add(newSensor);
			}
			else
			{
				// TODO : Log
				System.out.println(" Sensor is not physically valid !!");
			}
		}
		return newSensor;
	}	

	
	public static void main(String[] args) throws Exception
	{
		State stTester = new State();
		GenSensor testSens = stTester.addDevice("ADXL345");
		if(testSens!=null)
		{
			System.out.println("Success! "+testSens.getCommId()+ " "+ testSens.getIdentInfo().descr);
		}
		else
		{
			System.out.println("Ooops :( ");
		}
		
		try {Thread.sleep(2000);} catch (InterruptedException ie) {} // Wait for communication channel is up
		
		GenSensor testSens2 = stTester.addDevice("ADXL345");
		if(testSens2!=null)
		{
			System.out.println("Success! "+testSens2.getCommId()+ " "+ testSens2.getIdentInfo().descr);
		}
		else
		{
			System.out.println("Ooops :( ");
		}
		
		System.exit(0);
	}
}
