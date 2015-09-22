package paddlefish.def;


public class ControlInputValue 
{
	private static final int DEF_ID = -1;
	private static final Object DEF_VAL =  -1;
	// id of the value
	public int id;
	// value may be of type boolean or integer or double, 
	// That's why it is defined as an Object
	public Object value;
	
	public ControlInputValue()
	{
		this.id = DEF_ID;
		this.value = DEF_VAL;
	}
	
	public ControlInputValue(ControlInputValue val)
	{
		this.id = val.id;
		this.value = val.value;
	}
	
	public static void main(String[] args)
	{		
		/* TEST */
		ControlInputValue in = new ControlInputValue();
		in.value=true;
		System.out.println("value is: "+in.value);
		in.value=8;
		System.out.println("value is: "+in.value);
		in.value=2.3;
		System.out.println("value is: "+in.value);
	}
}
