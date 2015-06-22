package paddlefish.def;


public class SensorIdent 
{
	private static final String DEF_NAME = "<DEFAULT>";
	private static final String DEF_DESC =  "<DEFAULT>";
	private static final String DEF_MANUF =  "<DEFAULT>";
	private static final SensorCategory DEF_CATEGORY = SensorCategory.UNK;
	private static final SensorMeasurement DEF_UNIT = SensorMeasurement.UNK;
	private static final int DEF_DEVIDADRR = 0;
	private static final int DEF_DEVID = 0;
	private static final int DEF_OUTPUT = 0;
	
	public String devName;
	public String descr;
	public String manuf;
	public SensorCategory categ;
	public int deviceIDAddress;
	public int deviceID;
	public int outputCnt;
	public SensorMeasurement unit;
	
	public SensorIdent()
	{
		this.devName = DEF_NAME;
		this.descr   = DEF_DESC;
		this.manuf   = DEF_MANUF;
		this.categ   = DEF_CATEGORY;
		this.deviceID = DEF_DEVID;
		this.deviceIDAddress = DEF_DEVIDADRR;
		this.outputCnt = DEF_OUTPUT;
		this.unit = DEF_UNIT;
	}
	
	public SensorIdent(SensorIdent ident)
	{
		this();
		this.devName = ident.devName;
		this.descr   = ident.descr;
		this.manuf   = ident.manuf;
		this.categ   = ident.categ;
		this.deviceID = ident.deviceID;
		this.deviceIDAddress = ident.deviceIDAddress;
		this.outputCnt = ident.outputCnt;
		this.unit = ident.unit;
	}
	
}
