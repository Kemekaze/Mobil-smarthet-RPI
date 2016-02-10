package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
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
	    private StreamConnection streamConnection = null;
	    private StreamConnectionNotifier streamConnNotifier = null;
	    private ArrayList<RemoteDevice> clients;
	    private LocalDevice localDevice = null;
	    
	  	private boolean debug 		 = false;
	    private Thread thread;
	    private boolean isRunning = true;
	    
	    
		private BtServer(){
			uuid=new UUID("57e33d1fc1674f5aa94f5f0c58f49356", false);
			connectionURL = "btspp://localhost:"+uuid.toString()+";authenticate=false;encrypt=false;name=LeBox";
			clients = new ArrayList<RemoteDevice>();
			thread = new Thread (this, "Server");
			thread.start();
		}
		
		public void run() {
			try{
				
				start();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		public static BtServer get(){
			return (bt == null)?bt = new BtServer():bt;
		}
		public void debugOn(){
			debug = true;
		}
		public void debugOff(){
			debug = false;
		}
		private void start() throws IOException{			
			localDevice();
			streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionURL );
			print("Server up");
			print("Waiting for clients...");
			streamConnection=streamConnNotifier.acceptAndOpen();
			addClient(RemoteDevice.getRemoteDevice(streamConnection));
		}
		private void localDevice() throws BluetoothStateException{
			localDevice = LocalDevice.getLocalDevice();
			//localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			print("Local device discoverable: "+localDevice.getDiscoverable());
			print("Local device class: "+localDevice.getDeviceClass());
			print("Local device address: "+localDevice.getBluetoothAddress());
			print("Local device name: "+localDevice.getFriendlyName());
		}
		private void addClient(RemoteDevice client) throws IOException{			
			clients.add(client);
			print("Client connected");
			print("Remote device address: "+client.getBluetoothAddress());
			print("Remote device name: "+client.getFriendlyName(true));
		}
		
		private void print(String msg){			
			if(debug) System.out.println("["+this.getClass().getSimpleName()+"] "+msg);
		}
		public String read() throws IOException{
			if(streamConnection == null){
				throw new IOException("streamConnection is null");
			}
			InputStream inStream=streamConnection.openInputStream();
		    BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
		    String lineRead=bReader.readLine();
		    print(lineRead);
		    return lineRead;
		}
		public void send(String data) throws IOException{
			if(streamConnection == null){
				throw new IOException("streamConnection is null");
			}
			OutputStream outStream=streamConnection.openOutputStream();
			PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
			//pWriter.write("Response String from SPP Server\r\n");
			pWriter.write(data);
			pWriter.flush();
		}
}
