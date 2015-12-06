package paddlefish.def;

import java.util.ArrayList;

/* This enum defines constants for sensor categories this project implements*/
public enum SensorCategory {
	GYRO ("Gyroscope"),
	TEMP ("Temperature"),
	ACC ("Accelerometer"),
	COMPASS("Compass"),
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
	
	@SuppressWarnings("null")
	public static ArrayList<String> getCategoryList()
	{
		ArrayList<String> categoryList = null;
		
		categoryList.add("Accelerometer");
		categoryList.add("Gyroscope");
		categoryList.add("Temperature");
		categoryList.add("Compass");
		
		return categoryList;
		
	}
}
