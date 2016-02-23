package dat065.mobil_smarthet.api;

import com.phidgets.*;
import com.phidgets.event.*;
	
public class SenseMovement
{
	public static int count = 0;
	public final static int max = 2000;
	public static double[] xArray = new double[max];
	public final static int sensitivity = 20;
	private SensorChangeListener movmentChange;
	public static final void main(String args[]) throws Exception {		
		
		
		SpatialPhidget spatial;		
		spatial = new SpatialPhidget();
		spatial.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae){
				try
				{
					((SpatialPhidget)ae.getSource()).setDataRate(500); //400 - 496
																		//400 gör 1 mätning per 392-400 millis
				}
				catch (PhidgetException pe)
				{
					System.out.println("Problem setting data rate!");
				}
			}
		});
		spatial.addSpatialDataListener(new SpatialDataListener() {
			public void data(SpatialDataEvent sde) {
				int j;
				for(j=0;j<sde.getData().length;j++)
				{
					if(sde.getData()[j].getAcceleration().length>0)
					{
						System.out.println("X: " + Math.round((sde.getData()[j].getAcceleration()[0]) *100) 
										+ " Y: " + Math.round((sde.getData()[j].getAcceleration()[1]) *100)
										+ " Z: " + Math.round((sde.getData()[j].getAcceleration()[2]) *100) ); //Z not needed?
					}
				}

			}
		});

		spatial.openAny();
		System.out.println("waiting for Spatial attachment...");
		spatial.waitForAttachment();

		System.out.println("Serial: " + spatial.getSerialNumber());
		System.out.println("Accel Axes: " + spatial.getAccelerationAxisCount());
		
		System.out.println("Outputting events.  Input to stop.");
		System.in.read();
		System.out.print("closing...");
		spatial.close();
		spatial = null;
		System.out.println(" ok");
		if (false) {
			System.out.println("wait for finalization...");
			System.gc();
		}
		
	}
}
