package paddlefish.comm;

import java.util.HashMap;


public class I2CComm {
	// Count of available device addresses
	private int devAddrCnt;
	// Integer: device address (Ex: read as hexadecimal, but kept as decimal value)
	// Boolean: If the address is active
	private HashMap<Integer,Boolean> devAddrInf;
	
	// Default constructor
	public I2CComm()
	{
		devAddrCnt = 0;
		devAddrInf = new HashMap<Integer,Boolean>();
	}

	// Initialize devAddCnt already
	public I2CComm(int devAddrCnt)
	{
		this.devAddrCnt = devAddrCnt;
		devAddrInf = new HashMap<Integer,Boolean>();
	}
	
	public int getDevAddrCnt() {
		return devAddrCnt;
	}

	public void setDevAddrCnt(int devAddrCnt) {
		this.devAddrCnt = devAddrCnt;
	}

	public HashMap<Integer, Boolean> getDevAddrInf() {
		return devAddrInf;
	}

	public void setDevAddrInf(HashMap<Integer, Boolean> devAddrInf) {
		this.devAddrInf = devAddrInf;
	}
	
	public void addDeviceAddr(int addr, boolean isActive)
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
	
}