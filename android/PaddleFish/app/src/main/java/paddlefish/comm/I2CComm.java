package paddlefish.comm;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;


public class I2CComm {
	// Count of available device addresses
	private int devAddrCnt;
	// Byte: Device address 
	// Boolean: If the address is active
	private HashMap<Byte,Boolean> devAddrInf;

	// Default constructor
	public I2CComm()
	{
		devAddrCnt = 0;
		devAddrInf = new HashMap<Byte,Boolean>();
	}

	// Initialize devAddCnt already
	public I2CComm(int devAddrCnt)
	{
		this.devAddrCnt = devAddrCnt;
		devAddrInf = new HashMap<Byte,Boolean>();
	}

	public int getDevAddrCnt() {
		return devAddrCnt;
	}

	public void setDevAddrCnt(int devAddrCnt) {
		this.devAddrCnt = devAddrCnt;
	}

	public HashMap<Byte, Boolean> getDevAddrInf() {
		return devAddrInf;
	}

	public void setDevAddrInf(HashMap<Byte, Boolean> devAddrInf) {
		this.devAddrInf = devAddrInf;
	}

	public void addDeviceAddr(Byte addr, boolean isActive)
	{
		// If map is already full do not add
		if((this.devAddrCnt==this.devAddrInf.size())&&
				(this.devAddrCnt>0))
		{
			// We will write a simple log class for these stuff later
			System.out.println("DevAddrMap is already full");
		}else
		{
			devAddrInf.put(addr, isActive);
		}
	}

	/* Only one of the device addresses is active. Get the adress of the active one */
	public Byte getActiveDeviceAddr()
	{
		if(this.devAddrInf!=null)
		{
			for (Entry<Byte, Boolean> entry : devAddrInf.entrySet()) 
			{
				if (Objects.equals(true, entry.getValue())) 
				{
					return entry.getKey();
				}
			}
			//TODO:Log
			System.out.println("No active device address found");
		}
		//TODO:Log
		System.out.println("Device Info is not available");
		return null;
	}

	public void testI2CCommunicationInfo(I2CComm i2cc)
	{
		System.out.println("I2C INFO");
		if(i2cc!=null)
		{
			System.out.println("Number of available addresses: "+i2cc.getDevAddrCnt()+"   , DeviceAddressInformation  "+i2cc.getDevAddrInf().toString());
		}
		else
		{
			System.out.println("This device does not support I2CComm");
		}
	}
}
