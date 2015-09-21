package paddlefish.def;

//TODO: Add more measurement types
public enum SensorMeasurement 
{
	MG ("miligforce"), //  what is it really ?
	DEG_CEL ("celcius"),
	DEG_FAH ("fahrenheit"),
	DEG("degree"),
	UNK(""),
	;

	private final String descr;
	
	private SensorMeasurement(String descr)
	{
		this.descr = descr;
	}
	
	public String getDescription()
	{
		return this.descr;
	}
	
	// convert xml readings to enum
	public static SensorMeasurement getUnit(String munit)
	{
		switch(munit)
		{
			case "mg":
				return MG;
			case "celcius":
				return DEG_CEL;
			case "fahrenheit":
				return DEG_FAH;
			case "degree":
				return DEG;
			default:
				return UNK;
		}
	}
}


