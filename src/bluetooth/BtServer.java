package bluetooth;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	    private byte[] magicWord;
	    
		private BtServer(){
			magicWord = new byte[]{'a','a','n','d'};
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
			return (bt == null)?bt = new BtServer():bt;
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
		private class BtClient implements Runnable{
			private StreamConnection stream = null;
			private RemoteDevice device = null;
			private OutputStream out = null;
			private InputStream in = null;
			private boolean isRunning = true;
			private Thread thread = null;
			
			public BtClient(StreamConnection streamConnection)  throws IOException{	
				this.stream = streamConnection;
				this.out = streamConnection.openOutputStream();
				this.in = streamConnection.openInputStream();
				this.device = RemoteDevice.getRemoteDevice(streamConnection);
				this.thread = new Thread (this, getFriendlyName(true));		
				device.authorize(stream);
			}
			@Override
			public void run() {
				print(thread.getName()+" started");
				byte[] buffer = new byte[1024];				
		        int bytes;		        
				
				try {
					//wait for confirmation
					send("ack this".getBytes());
					//read ack with last recieved data
					bytes = in.read(buffer);
					byte[] data = read(buffer,bytes);
					//start sending data
					while(isRunning){
						thread.sleep(1000);
						send("Data should be here".getBytes());
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					stream.close();
					print(thread.getName()+" Socket closed");
				} catch (IOException e) {
					e.printStackTrace();
				}
				print(thread.getName()+" stopped");
			}
			public String getBluetoothAddress(){
				return this.device.getBluetoothAddress();
			}
			public String getFriendlyName(boolean b) throws IOException{
				return this.device.getFriendlyName(b);
			}
			public void stop(){
				this.isRunning = false;				
			}
			public void start(){
				thread.start();
			}
			
			private byte[] read(byte[] data, int bytes){
		        byte[] rtnArr = new byte[bytes];
		        for(int i = 0;i < bytes;i++){
		            rtnArr[i]=data[i];
		        }
		        print("Recieved: " + new String(rtnArr));
		        return rtnArr;
		    }
			public void send(byte[] data) throws IOException{					
				out.write(data);
				out.flush();
			}
			private void print(String msg){			
				System.out.println("["+this.getClass().getSimpleName()+"] "+msg);
			}
			
		}
}
