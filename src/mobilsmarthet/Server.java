package mobilsmarthet;


import mobilsmarthet.api.YoctopuceSensor;
import mobilsmarthet.bluetooth.*;
import mobilsmarthet.db.DB;


public class Server {

	public enum Sensors{
		TEMPERATURE(0x01,"Temperature"),
		LIGHT(0x02,"Light"),
		AUDIO(0x03,"Audio"),
		CO2(0x04,"Audio"),
		MOTION(0x01,"Motion");
		
		private final int id;
		private final String name;		
		private Sensors(int id, String name){
			this.id   = id;
			this.name = name;			
		}
		public String getName(){
			return name;
		}
		public int getId(){
			return id;
		}
		public static Sensors match(int id){
			for(Sensors s : Sensors.values())
				if(s.getId() == id) return s;
			return null;			
		}
		
	}
	
	private final DB db ;
	private final BtServer btServer;
	public Server(){

		db = DB.get();
		btServer = BtServer.get();

	}
	

	public static void main(String[] args) {
		Server server = new Server();
		YoctopuceSensor yocto = new YoctopuceSensor();
		yocto.start();
		

	}

}
