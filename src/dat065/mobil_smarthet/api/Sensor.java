package dat065.mobil_smarthet.api;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import dat065.mobil_smarthet.db.DB;
public class Sensor<T> {
	/*
	 * Phidget
	 * ljud,Rörelse
	 * Yoctopuce
	 * Ljus,c02,temp
	 */
	
	
	protected T sensor;
	private final String DEBUG_PREFIX;
	private final boolean DEBUG;
	private final Class cls;
	public Sensor(Class<T> cls, String prefix){
		this(cls, prefix, false);
	}
	public Sensor(Class<T> cls, String prefix, boolean debug){
		this.DEBUG = debug;
		this.DEBUG_PREFIX = prefix;
		this.cls = cls;
		try{				
			this.sensor = cls.newInstance();
			//DB.get().addSensor(this);
		}catch(InstantiationException | IllegalAccessException /*| IOException*/ e){
			e.printStackTrace();
		}
		
	}

	protected void print(String msg){
		if(DEBUG) System.out.println(DEBUG_PREFIX+" "+msg);
	}
	
	protected void printErr(String msg){
		System.err.println(DEBUG_PREFIX+" "+msg);
	}
	@SuppressWarnings({ "unchecked", "hiding" })
	public T get(){
		return sensor;
	}
	public String getClassName(){
		return cls.getName();
	}
	
	protected void write(String data) throws IOException{
		//DB.get().write(this, data);
		
	}
	public List<Integer> read() throws IOException{
		//return DB.get().read(this);
		return null;
	}

	

}
