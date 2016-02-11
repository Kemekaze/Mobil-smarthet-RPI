package mobilsmarthet;

import com.intel.bluetooth.*;
import java.util.ArrayList;
import javax.bluetooth.*;

import com.phidgets.*;

import api.*;
import bluetooth.*;
import db.DB;


public class Server {
	
	public Server(){
		
	}
	

	public static void main(String[] args) {
		//if(args[0] == "1")
			BtServer.get();
		/*else
			BtClient.get().debugOn();*/
	}

}
