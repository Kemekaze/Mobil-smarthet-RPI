package dat065.mobil_smarthet;


import dat065.mobil_smarthet.api.YoctopuceSensor;
import dat065.mobil_smarthet.bluetooth.*;
import dat065.mobil_smarthet.db.DB;
import dat065.mobil_smarthet.db.Populate;


public class Server {

	public enum Sensors{
		TEMPERATURE(0x01,"Temperature"),
		LIGHT(0x02,"Light"),
		AUDIO(0x03,"Audio"),
		CO2(0x04,"C02"),
		MOTION(0x05,"Motion");
		
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
		//YoctopuceSensor yocto = new YoctopuceSensor();
		//yocto.start();

	}
	

	public static void main(String[] args) {
		//Populate.populateSensor(Sensors.TEMPERATURE.getId(), 10, 20, 25);
		Server server = new Server();
	}

}
