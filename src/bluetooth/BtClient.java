package bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import db.DB;
import mobilsmarthet.Server;

public class BtClient implements Runnable{
	
	
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
			while(isRunning){
				bytes = in.read(buffer);
				byte[] data = read(buffer,bytes);
				
				byte type = data[0];
				byte sensor = data[1];
				int time = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(buffer, 2, 6)).getInt();
				switch(type){	
					case (byte) 0xFF:
						send(serialize(DB.get().getSensorsValue(time)));
						break;
					case (byte) 0x01:
						send(serialize(DB.get().getSensorValue((int)sensor,time)));
						break;
					default:
						send(serialize(new ArrayList<SerializableSensor>(){{add(new SerializableSensor(null,-1));}}));
						break;					
				}								
			}
			
		} catch (IOException e) {
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
	
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}			
	
	private void print(String msg){			
		System.out.println("["+this.getClass().getSimpleName()+"] "+msg);
		
	}

	
}