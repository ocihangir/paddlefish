package paddlefish.hal;

public interface CommControllerInterface {
	void commCommandReceiver(byte[] buffer);
	void commDataReceiver(byte[] buffer);
}
