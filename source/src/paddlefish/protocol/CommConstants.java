package paddlefish.protocol;

public class CommConstants {
	
	
	public static final char CMD_START = 0xA5;
	public static final char CMD_READ_BYTES = 0xC0;
	public static final char CMD_WRITE_BYTES = 0xC1;
	public static final char CMD_WRITE_BITS = 0xC2;
	public static final char CMD_NULL = 0x00;
	public static final char CMD_END = 0x0C;
	public static final char CMD_ESC = 0x0E;

	public static final char CMD_ANSWER = 0xA6;
}
