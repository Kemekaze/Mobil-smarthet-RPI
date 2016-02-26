package dat065.mobil_smarthet.api;

import java.util.ArrayList;







import com.mysql.fabric.xmlrpc.base.Array;
import com.yoctopuce.YoctoAPI.*;

import dat065.mobil_smarthet.api.*;
import dat065.mobil_smarthet.db.DB;

public class YoctopuceSensor implements Runnable{
	
	
	private boolean isRunning = false ,isInitialized = true;
	private ArrayList<YSensor> sensors = new ArrayList<>();
	private long interval = 10000;
	private Thread t;
	
	/**
	 *  creates an list of all connected yocto sensors.
	 */
	public YoctopuceSensor(){
		 
		try {
	         
			init();
			System.out.println("init "+ sensors.size()+ "sensors :"+ isInitialized);
	        } catch (YAPI_Exception ex) {
	            System.out.println("Cannot contact VirtualHub on 127.0.0.1 (" + ex.getLocalizedMessage() + ")");
	            System.out.println("Ensure that the VirtualHub application is running");
	        }
		t = new Thread(this);

	}
	
	/**
	 * method registers the virtual hub and adds all the connected yoctopuce sensors to sensors array.
	 * @return true if all three sensors are found and added to sensors else false
	 * @throws YAPI_Exception
	 */
	private boolean init() throws YAPI_Exception{
		// setup the API to use local VirtualHub
        YAPI.RegisterHub("127.0.0.1");
        YModule module = YModule.FirstModule();
        
        while(module != null){
        	
        	if(!module.getProductName().equals("VirtualHub")){
            	//System.out.println("sensor added: " + module.get_logicalName());
            	sensors.add(YSensor.FindSensor(module.get_logicalName()));
        	}
    		module = module.nextModule();
        }
        
        if(sensors.toArray().length == 3) return isInitialized = true; else return false;
	}
	
	/**
	 * writes the current value of the yocto sensors to corresponding table in db.
	 * 
	 */
	@SuppressWarnings("static-access")
	@Override
	public void run(){
		while(isRunning){
			for (int i = 0; i < sensors.size(); i++) {
				try {
					DB.get().addSensorvalue(sensors.get(i).get_module().getLogicalName(), sensors.get(i).get_currentValue());
					//System.out.println("sensor: " + sensors.get(i).get_module().get_logicalName()+"value: "+sensors.get(i).get_currentValue() );
				} catch (YAPI_Exception e) {
					System.out.println("Faild to fetch data from yoctoSensor");
				}

			}
			try {
				t.sleep(interval);
			} catch (InterruptedException e) {
			}
		}

	}
	/**
	 * stops the thread that update the yocto sensors 
	 */
	public void stop(){
		isRunning = false;
	}
	/**
	 * starts the the thread that update the yocto sensors 
	 */
	public void start(){
		isRunning = true; 
		t.start();
	}
	/**
	 * Sets the update rate for the sensors. 
	 * @param newRate new update rate in milliseconds. 
	 */
	public void setInterval (long newRate){
		interval = newRate;
	}
}	
	

