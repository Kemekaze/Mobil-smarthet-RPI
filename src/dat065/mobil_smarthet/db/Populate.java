package dat065.mobil_smarthet.db;

import java.util.Random;

public class Populate {
	private static Random r = new Random();
	
	public static void populateSensor(int sensor, int count, int min, int max){
		for(int i=0;i<count;i++){			
			DB.get().addSensorvalue(sensor, min+(max-min)*r.nextDouble());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
