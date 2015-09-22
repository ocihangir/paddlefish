/**
 * 
 */
package paddlefish.sensor;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import paddlefish.def.SensorCategory;

/**
 * @author Periphery
 *
 */
public class L3GD20 extends GenSensor {

	public L3GD20() throws Exception
	{
		super(SensorCategory.GYRO, "L3GD20");
	}

	/* (non-Javadoc)
	 * @see paddlefish.sensor.GenSensor#powerDown()
	 */
	@Override
	public boolean powerDown() throws IOException, InterruptedException,
			Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see paddlefish.sensor.GenSensor#powerUp()
	 */
	@Override
	public boolean powerUp() throws IOException, InterruptedException,
			Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see paddlefish.sensor.GenSensor#getStreamData()
	 */
	@Override
	public void getStreamData() {
		// TODO Auto-generated method stub

	}

}
