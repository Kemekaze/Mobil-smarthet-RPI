package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.*;
import javax.microedition.io.*;
public class BtAdapter implements Runnable {
    private final UUID uuid = new UUID("1101", true);
    private static BtAdapter bt = null;
    private String connectionString = "btspp://localhost:" + uuid +"";
    private String name="Sample SPP Server";
    
	private BtAdapter(){
		
	}
	public static BtAdapter get(){
		return (bt == null)?new BtAdapter():bt;
	}
	/**
	 * Starts the train
	 */
	@Override
	public void run() {				
		try {
			testConnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	public void testConnect() throws IOException{
		//open server url
		StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );

		//Wait for client connection
		System.out.println("\nServer Started. Waiting for clients to connect");
		StreamConnection connection=streamConnNotifier.acceptAndOpen();

		RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
		System.out.println("Remote device address: "+dev.getBluetoothAddress());
		System.out.println("Remote device name: "+dev.getFriendlyName(true));

		//read string from spp client
		InputStream inStream=connection.openInputStream();
		BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
		String lineRead=bReader.readLine();
		System.out.println(lineRead);

		//send response to spp client
		OutputStream outStream=connection.openOutputStream();
		PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
		pWriter.write("Response String from SPP Server\r\n");
		pWriter.flush();

		pWriter.close();
		streamConnNotifier.close();
	}
	
}