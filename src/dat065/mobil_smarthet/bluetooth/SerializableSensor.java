package dat065.mobil_smarthet.bluetooth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializableSensor implements Serializable{

	private static final long serialVersionUID = 197931937334569834L;
	private int sensor;
	private HashMap<Integer,Double> data;	
	
	public SerializableSensor(HashMap<Integer,Double> data, int sensor) throws IndexOutOfBoundsException{
		if( 5 >= sensor && sensor >= 0  )
			this.sensor = sensor;
		else throw new IndexOutOfBoundsException("Sensor value must be between 0-5: "+sensor);
		this.data = data;
	}
	public SerializableSensor(){

	}
	public int getSensor() {
		return sensor;
	}
	public void setSensor(int sensor) {
		this.sensor = sensor;
	}
	public Map<Integer, Double> getData() {
		return data;
	}
	public void setData(HashMap<Integer, Double> data) {
		this.data = data;
	}
	

}
