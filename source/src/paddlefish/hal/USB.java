package paddlefish.hal;

// Code in this class was taken from : 
// http://eclipsesource.com/blogs/2012/10/17/serial-communication-in-java-with-raspberry-pi-and-rxtx/

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class USB{
	InputStream in;
	OutputStream out;

	public USB(String portName, int baudRate) throws Exception
	{
		connect(portName, baudRate);
	}
	
	/*
	 * Connect to the serial port and hold it
	 */
	void connect( String portName, int baudRate ) throws Exception {
	    CommPortIdentifier portIdentifier = CommPortIdentifier
	        .getPortIdentifier( portName );
	    if( portIdentifier.isCurrentlyOwned() ) {
	      System.out.println( "Error: Port is currently in use" );
	    } else {
	      int timeout = 2000;
	      CommPort commPort = portIdentifier.open( this.getClass().getName(), timeout );
	 
	      if( commPort instanceof SerialPort ) {
	        SerialPort serialPort = ( SerialPort )commPort;
	        serialPort.setSerialPortParams( baudRate,
	                                        SerialPort.DATABITS_8,
	                                        SerialPort.STOPBITS_1,
	                                        SerialPort.PARITY_NONE );
	 
	        in = serialPort.getInputStream();
	        out = serialPort.getOutputStream();
	      } else {
	        System.out.println( "Error: Only serial ports are handled by this example." );
	      }
	    }
	  }
	
	public void sendData(char[] data) throws IOException
	{
		byte buffer[] = new byte[data.length];
		for (int i=0;i<data.length;i++)
			buffer[i] = (byte) data[i];
		this.out.write(buffer);
	}
	
	public byte[] receiveData() throws IOException
	{
		byte[] buffer = new byte[ 1024 ];
		this.in.read(buffer);
		return buffer;
	}
	
	public static class SerialReader implements Runnable {
		 
	    InputStream in;
	 
	    public SerialReader( InputStream in ) {
	      this.in = in;
	    }
	 
	    public void run() {
	      byte[] buffer = new byte[ 1024 ];
	      int len = -1;
	      try {
	        while( ( len = this.in.read( buffer ) ) > -1 ) {
	          System.out.print( new String( buffer, 0, len ) );
	        }
	      } catch( IOException e ) {
	        e.printStackTrace();
	      }
	    }
	  }
	 
	  public static class SerialWriter implements Runnable {
	 
	    OutputStream out;
	 
	    public SerialWriter( OutputStream out ) {
	      this.out = out;
	    }
	 
	    public void run() {
	      try {
	        int c = 0;
	        while( ( c = System.in.read() ) > -1 ) {
	          this.out.write( c );
	        }
	      } catch( IOException e ) {
	        e.printStackTrace();
	      }
	    }
	  }
}
