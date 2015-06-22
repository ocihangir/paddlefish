package paddlefish.def;

import java.util.ArrayList;

public class SensorControl 
{
	private static final ControlType DEF_CONTROL = ControlType.UNK;
	private static final String DEF_DESCR = "<DEFAULT>";
	
	public ControlType type;
	public String descr;
	// May have an array of control inputs
    public ArrayList<ControlInput> cInputs;
	
	public SensorControl()
	{
		this.type = DEF_CONTROL;
		this.descr = DEF_DESCR;
		this.cInputs = new ArrayList<ControlInput>();
	}
	
	public SensorControl(SensorControl control)
	{
		this();
		this.type = control.type;
		this.descr = control.descr;
		this.cInputs = control.cInputs;
	}
}
