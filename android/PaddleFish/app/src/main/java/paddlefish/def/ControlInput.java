package paddlefish.def;

import java.util.ArrayList;

public class ControlInput 
{	
	
	private static final String DEF_INTERFACE = "checkbox";
	private static final String DEF_NAME = "<DEFAULT>" ;	
	private static final int DEF_REG = 0 ;
	private static final int DEF_BIT = 0;
	private static final int DEF_LEN = 0;
	private static final int DEF_OPTIONSIZE = 0;
	private static final int DEF_OPTION = 0;
	
	// combo or checkbox
	public String interfaceType;
	// X Sleep, Y Sleep , ODR[Hz] - Cut-Off etc...
	public String name;
	//register for this control
	public byte register;
	// which bit?
	public int bit;
	// length of control
	public int length;
	// number of options available (number of values)
	public int noOptions;
	// default option
	public int defOption;
	// May have an array of input values (options) for the control
	public ArrayList<ControlInputValue> inValues;
	
	public ControlInput()
	{
		this.interfaceType = DEF_INTERFACE;
		this.name = DEF_NAME;
		this.register = DEF_REG;
		this.bit = DEF_BIT;
		this.defOption = DEF_OPTION;
		this.length = DEF_LEN;
		this.noOptions = DEF_OPTIONSIZE;
		this.inValues = new ArrayList<ControlInputValue>();
	}
	
	public ControlInput(ControlInput cIn)
	{
		this();
		this.interfaceType = cIn.interfaceType;
		this.name = cIn.name;
		this.register = cIn.register;
		this.bit = cIn.bit;
		this.defOption = cIn.defOption;
		this.length = cIn.length;
		this.noOptions = cIn.noOptions;
		this.inValues = cIn.inValues;
	}	
}
