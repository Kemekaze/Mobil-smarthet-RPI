package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.bluetooth.*;
import javax.microedition.io.*;
public class BtClient implements DiscoveryListener, Runnable {
	
    private static BtClient bt   = null;    
    private UUID[] uuidSet 		 = new UUID[1];    
  	private Object lock			 = new Object();
  	private Vector devices		 = new Vector();
  	private String connectionURL = null;
    private DiscoveryAgent agent = null;
    private StreamConnection streamConnection = null;
  	private boolean debug 		 = false;
  	private Thread thread;
    
	private BtClient(){
		uuidSet[0]=new UUID("446118f08b1e11e29e960800200c9a66", false);
		thread = new Thread (this, "Client");
		thread.start();
	}
	
	public void run() {
		try{
			searchDevices();
			printDevices();
			deviceServices(selectDevice());
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	public static BtClient get(){
		return (bt == null)?bt = new BtClient():bt;
	}
	public void debugOn(){
		debug = true;
	}
	public void debugOff(){
		debug = false;
	}
	private Vector getDevices(){
		return (devices.size()>0)? devices: null;
	}
	private void printDevices() throws IOException{
		int deviceCount = devices.size();
	    if(deviceCount <= 0){
	        print("No Devices Found .");
	    }
	    else{
	        print("Bluetooth Devices: ");
	        for (int i = 0; i <deviceCount; i++) {
	            RemoteDevice remoteDevice=(RemoteDevice)devices.elementAt(i);
	            print((i+1)+". "+remoteDevice.getBluetoothAddress()+" ("+remoteDevice.getFriendlyName(true)+")");
	        }
	    }
	    
	}
	private int selectDevice() throws IOException{
		 print("Choose Device index: ");
		 BufferedReader bReader=new BufferedReader(new InputStreamReader(System.in));
		 String chosenIndex=bReader.readLine();
		 return Integer.parseInt(chosenIndex.trim());
	}
	private void searchDevices() throws BluetoothStateException{

	    LocalDevice localDevice = LocalDevice.getLocalDevice();
	    print("Address: "+localDevice.getBluetoothAddress());
	    print("Name: "+localDevice.getFriendlyName());

	    agent = localDevice.getDiscoveryAgent();
	    print("Starting device inquiry...");
	    agent.startInquiry(DiscoveryAgent.GIAC, bt);
	    try {
	        synchronized(lock){
	            lock.wait();
	        }
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	private void deviceServices(int dev) throws IOException{
		RemoteDevice remoteDevice=(RemoteDevice)devices.elementAt(dev-1);
	    print("\nSearching for service...");
	    agent.searchServices(null,uuidSet,remoteDevice,bt);

	    try {
	        synchronized(lock){
	            lock.wait();
	        }
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }

	    if(connectionURL==null){
	        print("Device does not support Simple SPP Service.");
	    }else{
	    	streamConnection=(StreamConnection)Connector.open(connectionURL);
	    }
	}
	public void sendData() throws IOException{
		if(streamConnection == null){
			throw new IOException("streamConnection is null");
		}
	    OutputStream outStream=streamConnection.openOutputStream();
	    PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
	    pWriter.write("Test String from SPP Client\r\n");
	    pWriter.flush();

	}
	public String receiveData() throws IOException{
		if(streamConnection == null){
			throw new IOException("streamConnection is null");
		}
		InputStream inStream=streamConnection.openInputStream();
	    BufferedReader bReader2=new BufferedReader(new InputStreamReader(inStream));
	    String lineRead=bReader2.readLine();
	    print(lineRead);
	    return lineRead;
	}
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
	    if(!devices.contains(btDevice)){
	        devices.addElement(btDevice);
	    }
	}

	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
	    if(servRecord!=null && servRecord.length>0){
	        connectionURL=servRecord[0].getConnectionURL(0,false);
	    }
	    synchronized(lock){
	        lock.notify();
	    }
	}

	public void serviceSearchCompleted(int transID, int respCode) {
	    synchronized(lock){
	        lock.notify();
	    }
	}

	public void inquiryCompleted(int discType) {
	    synchronized(lock){
	    	print("Device Inquiry Completed. ");
	        lock.notify();
	    }
	}
	private void print(String msg){
		if(debug) System.out.println("["+this.getClass().getSimpleName()+"] "+msg);
	}
	
}