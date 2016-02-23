package dat065.mobil_smarthet.api;

import java.io.IOException;
import java.util.ArrayList;

import com.mysql.fabric.xmlrpc.base.Array;
import com.phidgets.*;
import com.phidgets.event.*;

import dat065.mobil_smarthet.Server.Sensors;
import dat065.mobil_smarthet.api.*;
import dat065.mobil_smarthet.db.DB;

public class PhidgetsSensor{
	private SpatialPhidget motionSensor;
	private double lastdB = 0;
	private double soundSensitivity = 2; // dB difference between last and current read which resaults in DB-write.
	private InterfaceKitPhidget ifk;
	private boolean isInitialized = true;
	private final int datarate = 480;
	
	/**
	 *  creates an list of all connected phidget sensors.
	 * @throws PhidgetException 
	 */
	public PhidgetsSensor(){	 
		try{
			init();
			System.out.println("init: "+ isInitialized);
		} catch(PhidgetException e){
			e.printStackTrace();
		}

	}
	
	/**
	 * init phidget sensors and sets listeners 
	 * @return true if both sensors are successfully init
	 * @throws PhidgetException
	 */
	private boolean init() throws PhidgetException{	
		motionSensor = new SpatialPhidget();
		ifk = new InterfaceKitPhidget();
		
		//Attach listeners to the motionSensor
        addMotionListeners();
        
		//Attach listeners to the soundSensor
        addSoundListeners();
        
        
        //Open and wait for attachment of sensors
        
        //MotionSensor attachment
        motionSensor.openAny();
		motionSensor.waitForAttachment();
		
		//SoundSensor attachment
        ifk.openAny();
		ifk.waitForAttachment();
		
		//Checks if sensors are connected
        if(motionSensor != null && ifk != null) return isInitialized = true; else return false;
	}
	/**
	 * adds listener for the motionsensor and writes data to the database.
	 */
	private void addMotionListeners(){
		motionSensor.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae){
				try
				{
					((SpatialPhidget)ae.getSource()).setDataRate(datarate); //400 - 496
																		//400 gör 1 mätning per 392-400 millis
				}
				catch (PhidgetException pe)
				{
					System.out.println("Problem setting data rate!");
				}
			}
		});
		motionSensor.addSpatialDataListener(new SpatialDataListener() {
			public void data(SpatialDataEvent sde) {
				int j;
				for(j=0;j<sde.getData().length;j++)
				{
					if(sde.getData()[j].getAcceleration().length>0)
					{
						/*
						DB.get().addSensorvalue(Sensors.MOTION.getName(), new double[]{ 
								Math.round((sde.getData()[j].getAcceleration()[0]) *100), 
								Math.round((sde.getData()[j].getAcceleration()[1]) *100),
								Math.round((sde.getData()[j].getAcceleration()[2]) *100)});
						*/
						
						System.out.println("X: " + Math.round((sde.getData()[j].getAcceleration()[0]) *100) 
										+ " Y: " + Math.round((sde.getData()[j].getAcceleration()[1]) *100)
										+ " Z: " + Math.round((sde.getData()[j].getAcceleration()[2]) *100) ); //Z not needed?
					}
				}

			}
		});
	}
	
	/**
	 * sets the sound listener and writes data to database.
	 */
	private void addSoundListeners(){
		ifk.addSensorChangeListener(new SensorChangeListener() {
			public void sensorChanged(SensorChangeEvent sensorChangeEvent){
				try
				{
					double tempdB = Math.round( 16.801*Math.log( sensorChangeEvent.getValue()) +9.872 );
					if((lastdB-tempdB) > soundSensitivity){
						/*
						 * DB.get().addSensorvalue(Sensors.AUDIO.getName(), lastdB); <<------
						 */
						System.out.println(" SOUND: "+(int) tempdB);
					}
					lastdB = tempdB;
	
				}
				catch (Exception e)
				{
					System.out.println("Problem fetching data from SoundSensor");
				}
			}
		});
		
	}	
	/**
	 * sets the sensitivity for which changes that going to be written the the DB.
	 * @param sensitivity in dB
	 */
	public void setSoundSensitivity(double sensitivity){
		soundSensitivity = sensitivity;
	}
	
	/**
	 * sets the rate in which the motion sensor reads.
	 * @param datarate
	 */
	public void setDataRateMotion(int datarate) {
		try {
			motionSensor.setDataRate(datarate);
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	}
	
	
}	
	
