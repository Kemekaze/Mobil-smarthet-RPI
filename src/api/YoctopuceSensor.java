package api;

import java.io.IOException;

import com.yoctopuce.YoctoAPI.*;

public class YoctopuceSensor<T extends YSensor> extends Sensor{
	

	private static final String DEBUG_PREFIX = "Yocto";
	private static boolean isInitialized;
	public YoctopuceSensor(Class<T> cls){
		this(cls,false);
	}
	public YoctopuceSensor(Class<T> cls, boolean debug){
		super(cls,DEBUG_PREFIX,debug);
		if(!isInitialized ){
			try {
				if(YAPI.SUCCESS == YAPI.RegisterHub("127.0.0.1")){
					isInitialized= true;
					print("Initialized");
				}
			} catch (YAPI_Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
}
