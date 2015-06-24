package paddlefish.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import paddlefish.comm.I2CComm;
import paddlefish.comm.SPIComm;
import paddlefish.def.ControlInput;
import paddlefish.def.ControlInputValue;
import paddlefish.def.ControlType;
import paddlefish.def.SensorCategory;
import paddlefish.def.SensorControl;
import paddlefish.def.SensorIdent;
import paddlefish.def.SensorMeasurement;
import paddlefish.def.SensorOutput;

public class SensorXMLReader 
{
	public static final String modelPath = "..\\..\\models\\sensors";
	// XML file path
	private String fName;
	// identification data read from XML file
	private SensorIdent identInfo;
	// Null, if device does not support I2C
	private I2CComm i2cInf;
	// Null, if device does not support SPI
	private SPIComm spiInf;
	// Output Information of Sensor
	private ArrayList<SensorOutput> outputLst;
	// Control Information of Sensor
	private ArrayList<SensorControl> controlLst;
	
	public SensorXMLReader(SensorCategory c, String devname) 
	{
		String filename = System.getProperty("java.class.path")+"\\"+modelPath+"\\"+c.getFolderName()+"\\"+devname+".xml";
		System.out.println(filename);
		this.fName=filename;
		identInfo = new SensorIdent();
		i2cInf = null;
		spiInf = null;
		outputLst = new ArrayList<SensorOutput>();
		controlLst = new ArrayList<SensorControl>();
	}
	
	private void readIdentification(Node identNode)
	{
		if(identNode!=null)
		{
			NodeList childNodes = identNode.getChildNodes();      
	        for (int j = 0; j < childNodes.getLength(); j++) 
	        {
	          Node cNode = childNodes.item(j);
	          if (cNode instanceof Element) 
	          {
	        	  switch(cNode.getNodeName())
	        	  {
	        	  	case "Name":  		
	        	  		identInfo.devName = cNode.getTextContent();	        	  		
	        	  		break;
	        	  	case "Description": 
	        	  		identInfo.descr = cNode.getTextContent();       	  		
	        	  		break;
	        	  	case "Manufacturer":   	  		
	        	  		identInfo.manuf = cNode.getTextContent();
	        	  		break;
	        	  	case "Category":
	        	  		identInfo.categ = SensorCategory.getCategory(cNode.getTextContent());
	        	  		break;
	        	  	default:
	        	  		break;
	        	  }
	          }
	        }
		}
		else
		{
			// TODO: LOG
			System.out.println("Null identification node");
		}
	}
	// Part of I2C info
	private void readDeviceAddrInfo(Node i2cAddrNode)
	{
		NodeList childNodes = i2cAddrNode.getChildNodes();  
		int addr = -1;
		int active = -1;
		
        for (int j = 0; j < childNodes.getLength(); j++) 
        {
          Node cNode = childNodes.item(j);
          if (cNode instanceof Element) 
          {
        	  switch(cNode.getNodeName())
        	  {			
	        	  case "addr":
	        		  addr = Integer.parseInt(cNode.getTextContent(), 16);
	        		  break;
	        	  case "active":
	        		  active = tryParse(cNode.getTextContent());
	        		  break;
	        	  default:
	        		  break;
        	  }
          }
        }
        // if successfully read from file
        if(addr!=-1 && active!=-1)
        {
        	i2cInf.addDeviceAddr(addr, (active==1));
        }
	}
	// Part of communication info
	private void readI2CInfo(Node i2cNode)
	{
		// if current device supports I2C 
		if(i2cInf!=null)
		{
			if(i2cNode!=null)
			{
				NodeList childNodes = i2cNode.getChildNodes();      
		        for (int j = 0; j < childNodes.getLength(); j++) 
		        {
		          Node cNode = childNodes.item(j);
		          if (cNode instanceof Element) 
		          {
		        	  switch(cNode.getNodeName())
		        	  {	
		        	  	case "DevAddressCnt":
		        	  		this.i2cInf.setDevAddrCnt(tryParse(cNode.getTextContent()));
		        	  		break;
		        	  	case "DevAddress":
		        	  		// read device address and if it is active
		        	  		this.readDeviceAddrInfo(cNode);
		        	  		break;
		                default:
		                	break;
		        	  }
		          }
		        }
			}
			else
			{
				//TODO: Log
				System.out.println("I2C Node is null");				
			}
		}
		else
		{
			//TODO: Log
			System.out.println("System does not support I2C");
		}
	}

	private void readCommunication(Node commNode)
	{
		if(commNode!=null)
		{
			NodeList childNodes = commNode.getChildNodes();      
	        for (int j = 0; j < childNodes.getLength(); j++) 
	        {
	          Node cNode = childNodes.item(j);
	          if (cNode instanceof Element) 
	          {
	        	  switch(cNode.getNodeName())
	        	  {
	        	  	case "Interface":
	        	  		if(cNode.getTextContent().equals("I2C"))
	        	  		{
	        	  			// So it supports I2C communication
	        	  			i2cInf = new I2CComm();
	        	  		}
	        	  		else if(cNode.getTextContent().equals("SPI"))
	        	  		{
	        	  			spiInf = new SPIComm();
	        	  		}
	        	  		else
	        	  		{
	        	  			//TODO: Log
	        	  			System.out.println("No such communication method");
	        	  		}
	        	  		break;
	        	  	case "I2C":
	        	  		this.readI2CInfo(cNode);
	        	  		break;
	        	  	//TODO: To be filled
	        	  	case "SPI":
	        	  		break;
	        	  	default:
	        	  		break;
	        	  }
	          }
	        }
		}
		else
		{
			// TODO: LOG
			System.out.println("Null communication node");
		}
	}
	
	private void readDeviceId(Node dNode)
	{
		NodeList childNodes = dNode.getChildNodes();  
		int addr = -1;
		int id = -1;
		
        for (int j = 0; j < childNodes.getLength(); j++) 
        {
          Node cNode = childNodes.item(j);
          if (cNode instanceof Element) 
          {
        	  switch(cNode.getNodeName())
        	  {			
	        	  case "address":
	        		  addr = Integer.parseInt(cNode.getTextContent(), 16);
	        		  break;
	        	  case "ID":
	        		  id =  Integer.parseInt(cNode.getTextContent(), 16);
	        		  break;
	        	  default:
	        		  break;
        	  }
          }
        }
        // if successfully read from file
        if(addr!=-1 && id!=-1)
        {
        	this.identInfo.deviceID = id;
        	this.identInfo.deviceIDAddress = addr;
        }
        //TODO: Log
        else
        {
        	System.out.println("Can not read device id");
        }
	}
	
	private void readOutputInfo(Node outputInfo)
	{
		if(outputInfo!=null)
		{
			SensorOutput output = new SensorOutput();
			output.outputId = tryParse(((Element) outputInfo).getAttribute("no"));
			output.descr = ((Element) outputInfo).getAttribute("descr");
			output.dType = ((Element) outputInfo).getAttribute("dataType");
			output.length = tryParse(((Element) outputInfo).getAttribute("len"));
			output.lsb = tryParse(((Element) outputInfo).getAttribute("lsb"));
			output.res= tryParse(((Element) outputInfo).getAttribute("res"));
			// read hexadecimal as decimal!
			output.register = Integer.parseInt(((Element) outputInfo).getAttribute("register"), 16);
			// add to list
			outputLst.add(output);
		}else
		{
			//TODO: Log
		}
		
	}
	
	private void readData(Node dataNode)
	{
		if(dataNode!=null)
		{
			NodeList childNodes = dataNode.getChildNodes();      
	        for (int j = 0; j < childNodes.getLength(); j++) 
	        {
	          Node cNode = childNodes.item(j);
	          if (cNode instanceof Element) 
	          {
	        	  switch(cNode.getNodeName())
	        	  {
	        	  	case "DeviceID":
	        	  		this.readDeviceId(cNode);
	        	  		break;
	        	  	case "OutputCnt":
	        	  		this.identInfo.outputCnt = tryParse(cNode.getTextContent());
	        	  		break;
	        	  	case "Unit":
	        	  		this.identInfo.unit = SensorMeasurement.getUnit(cNode.getTextContent());
	        	  		break;
	        	  	case "Output":
	        	  		this.readOutputInfo(cNode);
	        	  		break;
	        	  }
	          }
	        }
		}
		else
		{
			//TODO: Log
		}
	}
	
	private ControlInput readControlInput(Node controlIn)
	{
		if(controlIn!=null)
		{
			// new control input
			ControlInput cIn = new ControlInput();
			// read name attribute
			cIn.name = ((Element) controlIn).getAttribute("name");
			// read interface type
			cIn.interfaceType = ((Element) controlIn).getAttribute("type");
			// read register (read as hexadecimal but kept as decimal)
			cIn.register =  Integer.parseInt(((Element) controlIn).getAttribute("register"),16);
			// read bit
			cIn.bit = tryParse(((Element) controlIn).getAttribute("bit"));
			// read length
			cIn.length =  tryParse(((Element) controlIn).getAttribute("len"));
			// read number of options
			cIn.noOptions = tryParse(((Element) controlIn).getAttribute("cnt"));
			// read default option
			cIn.defOption = tryParse(((Element) controlIn).getAttribute("def"));
			// read values
			NodeList childNodes = controlIn.getChildNodes();
	        for (int j = 0; j < childNodes.getLength(); j++) 
	        {
	          Node cNode = childNodes.item(j);
	          if (cNode instanceof Element && (cNode.getNodeName().equals("val"))) 
	          {
	        	  // Add ControlInputValue to ControlInput's value list
	        	  ControlInputValue iVal = new ControlInputValue();
	        	  iVal.id = tryParse(((Element) cNode).getAttribute("id"));
	        	  // maybe a true/false value, or a double value, should be checked !
	        	  iVal.value = cNode.getTextContent();
	        	  cIn.inValues.add(iVal);
	          }
	        }
			return cIn;
		}
		else
		{
			//TODO: Log
			return null;
		}
	}
	
	private void readControl(Node control)
	{
		if(control!=null)
		{
			// new control
			SensorControl cont = new SensorControl();
			// read type attribute
			cont.type = ControlType.getControlType(((Element) control).getAttribute("type"));
			// read description
			cont.descr = ((Element) control).getAttribute("descr");
			NodeList childNodes = control.getChildNodes();
	        for (int j = 0; j < childNodes.getLength(); j++) 
	        {
	          Node cNode = childNodes.item(j);
	          if (cNode instanceof Element && (cNode.getNodeName().equals("input"))) 
	          {
	        	  // Add ControlInput to SensorControl's input list
	        	  cont.cInputs.add(this.readControlInput(cNode));
	          }
	        }
	        // Add SensorControl to control list
	        this.controlLst.add(cont);
		}
		else
		{
			//TODO: Log
		}
	}
	
	private void readDeviceControl(Node devControlNode)
	{
		if(devControlNode!=null)
		{
			NodeList childNodes = devControlNode.getChildNodes();      
	        for (int j = 0; j < childNodes.getLength(); j++) 
	        {
	          Node cNode = childNodes.item(j);
	          if (cNode instanceof Element && (cNode.getNodeName().equals("Control"))) 
	          {
	        	  this.readControl(cNode);
	          }
	        }
		}
		else
		{
			//TODO: Log
		}		
	}
	
	public void readFile() throws SAXException, IOException, ParserConfigurationException
	{
	    DocumentBuilderFactory factory = 
		        DocumentBuilderFactory.newInstance();
		//Get the DOM Builder
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    //Load and Parse the XML document
	    //document contains the complete XML as a Tree.
	    File xmlFile = new File(this.fName);
	    
	    Document curDocument = 
	      builder.parse(xmlFile);
	    
	    NodeList nodeList = curDocument.getDocumentElement().getChildNodes();
		
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        //We have encountered an <device> tag.
	        Node node = nodeList.item(i);
	        if (node instanceof Element)
	        {
	        	String nodeName = node.getNodeName();
	        	switch(nodeName)
	        	{
	        		case "Identification":
	        			this.readIdentification(node);
	        			break;
	        		case "Communication":
	        			this.readCommunication(node);
	        			break;
	        		case "Data":
	        			this.readData(node);
	        			break;
	        		case "DeviceControl":
	        			this.readDeviceControl(node);
	        			break;
	        		default:
	        			break;
	        	}
	        }
	    }	
	}
	
	public SensorIdent getIdentInfo() {
		return identInfo;
	}

	public I2CComm getI2cInf() {
		return i2cInf;
	}

	public SPIComm getSpiInf() {
		return spiInf;
	}

	public ArrayList<SensorOutput> getOutputLst() {
		return outputLst;
	}

	public ArrayList<SensorControl> getControlLst() {
		return controlLst;
	}

	public static Integer tryParse(String text) {
		 try 
		 {
		    return new Integer(text);
		 } 
		 catch (NumberFormatException e)
		 {
		    return 0;
		 }
	}
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException  
	{		
		/* TEST */
		SensorXMLReader reader = new SensorXMLReader(SensorCategory.ACC, "ADXL345");
		reader.readFile();
	}
}
