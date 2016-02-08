import com.phidgets.InterfaceKitPhidget;


public class Main{
	
    private static SoundChange sensor_listener;
    private static InterfaceKitPhidget ifk;


	public static void main(String[] args) {
		
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
        
		try {
            ifk = new InterfaceKitPhidget();
            sensor_listener = new SoundChange();
            ifk.addSensorChangeListener(sensor_listener);
            ifk.openAny();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
