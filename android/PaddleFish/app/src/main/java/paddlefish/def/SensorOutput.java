package paddlefish.def;

import java.util.ArrayList;

public class SensorOutput 
{
	private static final int DEF_ID = -1;
	private static final String DEF_DESC =  "<DEFAULT>";
	private static final int DEF_LENGTH = 0;
	private static final int DEF_LSB = 0;
	private static final int DEF_RES = 10;
	private static final int DEF_REG = 0;
	private static final String DEF_DATATYPE = "BYTE";
	/**
	 *  If sensor has multiple outputs, find the correct output from its id
	 */
	public int outputId;
	public String descr;
	/* length of output in bytes*/
	public int length;
	// least significant bit of the output
	public int lsb;
	// resolution in bits
	public int res;
	// address of the register where the output will be read
	public byte register;
	// data type of output
	public String dType;

	/**
	 *  Sensor physical values
	 */
	private byte[] outputValue;
	
	public byte[] getOutputValue()
	{
		return outputValue;		
	}
	
	public void setOutputValue(byte[] value)
	{
		
		outputValue = value.clone();		
	}
	
	public SensorOutput() {
		// TODO Auto-generated constructor stub
		this.outputId = DEF_ID;
		this.descr = DEF_DESC;
		this.length = DEF_LENGTH;
		this.lsb = DEF_LSB;
		this.res = DEF_RES;
		this.register = DEF_REG;
		this.dType = DEF_DATATYPE;
	}

	public SensorOutput(SensorOutput output)
	{
		this();
		this.outputId = output.outputId;
		this.descr = output.descr;
		this.length = output.length;
		this.lsb = output.lsb;
		this.res = output.res;
		this.register = output.register;	
		this.dType = output.dType;
	}
	
	public void testOutputInfo(SensorOutput so)
	{
		System.out.println("Available Output:");
		System.out.println(so.descr +" Type:  "+so.dType+"  Length: "+so.length+" Resolution: "+ so.res+" bits");
	}
}
