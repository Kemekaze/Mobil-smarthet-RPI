package bluetooth;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BtServer implements Runnable{
	
 	private static BtServer bt   = null;    
 	
    private UUID uuid =null;;    
  	private String connectionURL = null;	  	
    private StreamConnectionNotifier streamConnNotifier = null;
    private ArrayList<BtClient> clients;
    private LocalDevice localDevice = null;
    
  	private boolean debug 		 = true;
    private Thread thread;
    private boolean isRunning = true;
    
	private BtServer(){
		uuid=new UUID("57e33d1fc1674f5aa94f5f0c58f49356", false);
		connectionURL = "btspp://localhost:"+uuid.toString()+";authenticate=false;encrypt=false;name=LeBox";
		print(connectionURL);
		clients = new ArrayList<BtClient>();
		thread = new Thread (this, "Bluetooth Server");
	}
	
	public void run() {
		try{				
			init();
			while(isRunning){
				print("Waiting for Clients...");
				StreamConnection streamConnection = streamConnNotifier.acceptAndOpen();
				addClient(streamConnection);					
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	public static BtServer get(){
		if(bt == null){
			bt = new BtServer();
			bt.start();				
		}
		return bt;
	}
	public void debugOn(){
		debug = true;
	}
	public void debugOff(){
		debug = false;
	}
	private void init() throws IOException{	
		localDevice = LocalDevice.getLocalDevice();
		localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		print("Local device address: "+localDevice.getBluetoothAddress());
		print("Local device name: "+localDevice.getFriendlyName());			
		streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionURL );
		print("Server up");
	}
	public void stop(){
		this.isRunning = false;				
	}
	public void start(){
		thread.start();
	}
	
	private void addClient(StreamConnection streamConnection) throws IOException{		
		BtClient client = new BtClient(streamConnection);
		print("Client connected");
		print("Remote device address: "+client.getBluetoothAddress());
		print("Remote device name: "+client.getFriendlyName(true));
		clients.add(client);
		client.start();		
	}
	
	public ArrayList<BtClient> getClients(){
		return clients;
	}
	
	private void print(String msg){			
		if(debug) System.out.println("["+this.getClass().getSimpleName()+"] "+msg);
	}	
}
