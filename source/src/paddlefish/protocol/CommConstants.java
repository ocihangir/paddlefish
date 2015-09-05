package paddlefish.protocol;

public class CommConstants {
	
	
	// i2c
	public static final byte CMD_READ_BYTES = (byte) 0xC0;
	public static final byte CMD_WRITE_BYTES = (byte) 0xC1;
	public static final byte CMD_WRITE_BITS = (byte) 0xC2;
	public static final byte CMD_SET_I2C_SPEED = (byte) 0xC3;
	// stream
	public static final byte CMD_STREAM_ON = (byte) 0xB0;
	public static final byte CMD_STREAM_ADD = (byte) 0xB1;
	public static final byte CMD_STREAM_RST = (byte) 0xB2;
	public static final byte CMD_STREAM_PERIOD = (byte) 0xBF;
	public static final byte CMD_STREAM_START = (byte) 0xBE;
	public static final byte CMD_STREAM_END = (byte) 0xBD;
	// basic
	public static final byte CMD_START = (byte) 0xA5;
	public static final byte CMD_ANSWER = (byte) 0xA6;
	public static final byte CMD_DATA_ANSWER = (byte) 0xA7;
	public static final byte CMD_DATA_TEST = (byte) 0xAF;
	public static final byte CMD_NULL = (byte) 0x00;
	public static final byte CMD_END = (byte) 0x0C;
	public static final byte CMD_ESC = (byte) 0x0E;
	public static final byte CMD_OK = (byte) 0x0D;
}
