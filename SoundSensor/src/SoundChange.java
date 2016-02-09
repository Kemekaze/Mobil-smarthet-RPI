/* - IFKitSensorChangeListener - 
 * Set the textbox content based on the input index that is communicating
 * with the interface kit
 *
 * Copyright 2007 Phidgets Inc.  
 * This work is licensed under the Creative Commons Attribution 2.5 Canada License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by/2.5/ca/
 */

import com.phidgets.event.SensorChangeListener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.phidgets.event.SensorChangeEvent;
/**
 *
 * @author Owner
 */
public class SoundChange implements SensorChangeListener{
	
	int dB;

    public void sensorChanged(SensorChangeEvent sensorChangeEvent)
    {
	    		//System.out.println(sensorChangeEvent.getValue());
	    		try (	FileWriter output = new FileWriter("text.txt", true)) {
	    			dB = (int) Math.round( 16.801*Math.log( sensorChangeEvent.getValue()) +9.872 );
	    			//output.write(dB + ",");
	    			System.out.println(dB);
	    			
	    		} catch (IOException e) {
					e.printStackTrace();
				}
    }
    
}