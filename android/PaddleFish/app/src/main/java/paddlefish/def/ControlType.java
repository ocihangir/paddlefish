package paddlefish.def;
// Controls defined in sensor XML Files
public enum ControlType 
{
	POWERDOWN ("Power Down the device"),
	MEASURE ("Measure Activity"),
	SLEEP ("Go to sleep"),
	WAKEUP("Wake up the device"),
	CUTOFF("Cut Off Frequency"),
	UNK(""),
	;
	// description for actions	
	private final String descr;
	
	private ControlType(String descr)
	{
		this.descr = descr;
	}
	
	public String getDescr()
	{
		return descr;
	}
	
	// convert XML readings to Enum for convenience
	public static ControlType getControlType(String control)
	{
		switch(control)
		{
			case "powerdown":
				return POWERDOWN;
			case "sleep":
				return SLEEP;
			case "cutoff":
				return CUTOFF;
			case "measure":
				return MEASURE;
			case "wakeup":
				return WAKEUP;
			default:
				return UNK;
		}
	}
}
