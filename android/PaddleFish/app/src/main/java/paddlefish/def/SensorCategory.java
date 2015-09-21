package paddlefish.def;

/* This enum defines constants for sensor categories this project implements*/
public enum SensorCategory {
	GYRO ("gyroscope"),
	TEMP ("temperature"),
	ACC ("accelerometer"),
	COMPASS("compass"),
	UNK(""),
	;
	// folder name under models/sensors	
	private final String folderName;
	
	private SensorCategory(String folderName)
	{
		this.folderName = folderName;
	}
	
	public String getFolderName()
	{
		return this.folderName;
	}
	// convert XML readings to Enum for convenience
	public static SensorCategory getCategory(String name)
	{
		switch(name)
		{
			case "Accelerometer":
				return ACC;
			case "Gyroscope":
				return GYRO;
			case "Temperature":
				return TEMP;
			case "Compass":
				return COMPASS;
			default:
				return UNK;
		}
	}
}
