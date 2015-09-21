package paddlefish.hal;

public interface CommReceiverInterface {
	void commCommandReceiver(byte[] buffer, Object receiver);
	void commDataReceiver(byte[] buffer, Object receiver);
}
