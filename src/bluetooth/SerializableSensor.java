package bluetooth;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializableSensor implements Serializable{
	
	private static final long serialVersionUID = 8533081634202964645L;
	
	private final int sensor;
	private final Map<Integer,Double> data;	
	
	public SerializableSensor(Map<Integer,Double> data, int sensor) throws IndexOutOfBoundsException{
		if( 4 >= sensor && sensor >= 0  )
			this.sensor = sensor;
		else throw new IndexOutOfBoundsException("Sensor value must be between 0-4");
		this.data = data;
	}

}
