package dat065.mobil_smarthet.api;

import java.io.IOException;

import com.phidgets.*;
import com.phidgets.event.*;

public class PhidgetsSensor<T extends Phidget> extends Sensor{
	private static final String DEBUG_PREFIX = "Phidget";
	
	public PhidgetsSensor(Class<T> cls) throws InstantiationException, IllegalAccessException, PhidgetException, IOException{
		this(cls,false);		
	}
	
	public PhidgetsSensor(Class<T> cls, boolean debug) throws InstantiationException, IllegalAccessException, PhidgetException, IOException{
		super(cls,DEBUG_PREFIX,debug);
		sensor().openAny();
		sensor().waitForAttachment();
		addListeners();
		
	}
	private Phidget sensor(){
		return ((Phidget) sensor);
	}
	private void addListeners(){
		sensor().addAttachListener(e->{
			print("Attachment: " + e);			
		});
		sensor().addDetachListener(e->{
			print("Detachment: " + e);			
		});
		sensor().addErrorListener(e->{
			printErr("Error: "+e);	
		});
	}
	

	

}
