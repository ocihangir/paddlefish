package paddlefish.run;

import android.content.res.AssetManager;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import paddlefish.def.SensorCategory;
import paddlefish.io.SensorXMLReader;
import paddlefish.sensor.*;

public class State {
    /* Only this class will access and read assets*/
	private AssetManager astMng;
	// Keeps currently active sensors in the list
	private ArrayList<GenSensor> activeSensLst;
	// number of sensors currently in the system
	private int sensCnt;
    // all sensor files in the assets
    private String[] sensorNameLst;

	// Return active sensor list
    /*
	public ArrayList<SensorXMLReader> getAvailableSensorList()
	{
		ArrayList<SensorXMLReader> availableSensors = null;
		
		// TODO : automatize sensor listing process
		
		SensorCategory sensCategory = null;
		// ArrayList<String> categories = sensCategory.getCategoryList();
		
		SensorXMLReader xmlReader = new SensorXMLReader(sensCategory.getCategory("Accelerometer"),"ADXL345");
		availableSensors.add(xmlReader);
		
		xmlReader = new SensorXMLReader(sensCategory.getCategory("Gyroscope"),"L3GD20");
		availableSensors.add(xmlReader);
		
		return availableSensors;
	}
	*/
	public State(AssetManager ast) throws SAXException, ParserConfigurationException, URISyntaxException {
		sensCnt = 0;
		activeSensLst = new ArrayList<GenSensor>();

        this.astMng = ast;

        try
        {
            sensorNameLst = astMng.list("models");
            Log.d("Sensor names are read",sensorNameLst[0]);
            getDevicesOfCat(SensorCategory.GYRO);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}

	public ArrayList<GenSensor> getActiveSensLst() {
		return this.activeSensLst;
	}

    // Reorganized for Assets folder
	public ArrayList<String> getDevicesOfCat(SensorCategory cat) throws SAXException, ParserConfigurationException, URISyntaxException {
		ArrayList<String> devNames = new ArrayList<String>();
        // sensorNameLst içerisindeki xml leri okuyup kategorilerine göre ayırıyoruz
        InputStream input;

        if(this.sensorNameLst.length>0) {
            // for all asset files
            for(String senName:this.sensorNameLst)
            {
                try
                {
                    input = this.astMng.open("models/"+senName);
                    SensorXMLReader newSensInfo = new SensorXMLReader(input);
                    newSensInfo.readFile();
                    String desc = newSensInfo.getIdentInfo().descr;
                    Log.d("State get Category", desc);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return devNames;
	}

	/*
	 * devname: Available codable devices with XML files return newSensor: new
	 * Sensor added to the system
	 */
	public GenSensor addDevice(String devname) throws Exception {
		GenSensor newSensor = null;
		switch (devname) 
		{
		case "ADXL345":
			newSensor = new ADXL345();
            newSensor.initFromXML(this.astMng.open("models/ADXL345"));
			break;
		case "L3GD20":
			newSensor = new L3GD20();
            newSensor.initFromXML(this.astMng.open("models/L3GD20"));
			break;
		case "Test":
			newSensor = new TestSensor();
			break;
		default:
			// TODO: Log!
			System.out.println("Devname does not exist");
			break;
		}
		if (newSensor != null && !devname.equals("Test")) {
			// assign a commId (important to send & receive messages from queue)
			newSensor.setCommId(sensCnt++);
			// check if it is valid
			if (newSensor.checkPhysicalValid()) {
				// add it to active sensor list
				activeSensLst.add(newSensor);
			} else {
				// TODO : Log
				System.out.println(" Sensor is not physically valid !!");
			}
		}
		return newSensor;
	}
}
