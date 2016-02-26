package dat065.mobil_smarthet.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import dat065.mobil_smarthet.db.DB;


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
		print("Connected");
		byte[] buffer = new byte[1024];				
        int bytes;		        
		
		try {			
			while(isRunning){
				bytes = in.read(buffer);
				byte[] data = null;
				try{
					data = read(buffer,bytes);
				}catch(NegativeArraySizeException e){
					print("Disconnected");
					stop();
					continue;
				}
				
				byte type = data[0];
				byte sensor = data[1];
				long time = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(buffer, 2, 10)).getLong();
				print("Time: "+time);
				byte [] request;
				switch(type){	
					case (byte) 0xFF:
						request = serialize(DB.get().getSensorsValue(time));
						break;
					case (byte) 0x01:
						request = serialize(DB.get().toArrayList(DB.get().getSensorValue((int)sensor,time)));
						break;
					default:
						SerializableSensor s = new SerializableSensor(null,0);
						request = serialize(DB.get().toArrayList(s));
						break;					
				}
				send(request);								
			}
			
		} catch (IOException e) {
			e.printStackTrace();					
		}
		try {
			stream.close();
			print("Socket closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	private byte[] read(byte[] data, int bytes) throws NegativeArraySizeException{
		
		
        byte[] rtnArr = new byte[bytes];
        for(int i = 0;i < bytes;i++){
            rtnArr[i]=data[i];
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : rtnArr) {
            sb.append(String.format("%02X ", b));
        }
        
        print("Recieved: " + sb.toString());
        return rtnArr;
    }
	public void send(byte[] data) throws IOException{
		print("Sending: "+ data.length);
		out.write(data);
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
		System.out.println("["+this.getClass().getSimpleName()+" - "+thread.getName()+"] "+msg);
		
	}

	
}
