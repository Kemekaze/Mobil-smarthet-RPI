package mobilsmarthet;

import java.util.ArrayList;
import javax.bluetooth.*;

import com.phidgets.*;

import api.*;
import bluetooth.BtAdapter;
import db.DB;


public class Server {
	
	public Server(){
		
	}
	

	public static void main(String[] args) {
		Thread t1 = new Thread (BtAdapter.get(), "train1");
		t1.start();
	}

}
