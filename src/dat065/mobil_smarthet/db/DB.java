package dat065.mobil_smarthet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import dat065.mobil_smarthet.Server.Sensors;
import dat065.mobil_smarthet.bluetooth.SerializableSensor;





public class DB {	

	private static DB db = null;
	private Connection con = null; 
	private boolean debug 	= true;
	private DB(){
		String dbUrl = "localhost";
        String dbPort = "3306";
        String dbName = "DAT065";
        String dbUserName = "root";
        String dbPassword = "notRandom";
        String conString = "jdbc:mysql://"+dbUrl+":"+dbPort+"/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword;
        try {        
            con = DriverManager.getConnection(conString);       
            print("Connected to '"+dbName+"'as "+dbUserName+"@"+dbUrl);
        } catch (SQLException ex) {         
        	ex.printStackTrace();
        }
	}
	public void close(){
		try {
            if (con != null) {
                con.close();
                con = null;
                db = null;
            }
        }catch(SQLException ex) {
        	ex.printStackTrace();
        }
	}
	
	public static DB get(){
		if(db == null){
			db = new DB();
		}
		return db;
	}
	public void debugOn(){
		debug = true;
	}
	public void debugOff(){
		debug = false;
	}	
	
	public ArrayList<SerializableSensor> getSensorsValue(Long time){		
		ArrayList<SerializableSensor> list = new ArrayList<SerializableSensor>();
		for(Sensors s : Sensors.values()){
			SerializableSensor ss = getSensorValue(s.getId(),time);		
			list.add(ss);
		}
		return list;
	}
	public ArrayList<SerializableSensor> toArrayList(SerializableSensor sensor){
		ArrayList<SerializableSensor> list = new ArrayList<SerializableSensor>();	
		list.add(sensor);		
		return list;
	}
	
	public SerializableSensor getLastSensorValue(int sensor){
		Sensors s = Sensors.match(sensor);
		if(s == null) return null;
		String query = "SELECT * FROM "+sensor+"LIMIT 1";
		HashMap<Long,Double> vals = getSensorValue(query);
		SerializableSensor ss = new SerializableSensor(vals,s.getId());
		return ss;
	}
	
	public SerializableSensor getSensorValue(int sensor, Long time) throws NullPointerException{
		Sensors s = Sensors.match(sensor);
		if(s == null) return null;
		String query = "SELECT * FROM "+s.getName()+" WHERE `time` > "+time+" ORDER BY time";
		HashMap<Long,Double> vals = getSensorValue(query);
		SerializableSensor ss = new SerializableSensor(vals,s.getId());
		return ss;
	}
	
	private HashMap<Long,Double> getSensorValue(String query){
		HashMap<Long,Double> data = new HashMap<Long,Double>();
		try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
            	data.put(rs.getLong(1), rs.getDouble(2));
            }
            rs.close();
        	st.close();        	
        }catch(SQLException ex) {
        	ex.printStackTrace();
        }
		return data;
	}

	public void addSensorvalue(int sensor, double value){
		Sensors s = Sensors.match(sensor);
		if(s == null) return;
		addSensorvalue(s.getName(), new double[]{value});
	}
	public void addSensorvalue(String sensor, double value){
		addSensorvalue(sensor, new double[]{value});
	}
	public void addSensorvalue(int sensor, double[] values){
		Sensors s = Sensors.match(sensor);
		if(s == null) return;
		addSensorvalue(s.getName(),values);
	}
	
	public void addSensorvalue(String sensor, double[] values){
		String query  = "INSERT INTO "+sensor;
		String params = " (time";
		String vals = ") VALUES ('"+System.currentTimeMillis()+"'";
		int i = 1;
		for(double val: values){
			params += ",value"+((i>1)?i:"");
			vals   += ",'"+val+"'";
			i++;
		}		
		query+=params+vals+")";
		//print(query);
		/*String query = "INSERT INTO "+sensor+" (time,value)"+
		               " VALUES ('"+Instant.now().getEpochSecond()+"','"+value+"')";*/
		try {
			Statement st = con.createStatement();
            st.executeUpdate(query);
            st.close();
            print("Inserted into "+ sensor);
        }catch(SQLException ex) {
        	ex.printStackTrace();           
        }
	}
	public boolean emptySensorValues(int sensor){
		Sensors s = Sensors.match(sensor);
		if(s == null) return false;
		return emptySensorValues(s.getName());
	}
	public boolean emptySensorValues(String sensor){
		String query = "TRUNCATE TABLE "+sensor;
		try {
			Statement st = con.createStatement();
            st.executeUpdate(query);
            st.close();
            print("Truncated "+ sensor);
            return true;
        }catch(SQLException ex) {
        	ex.printStackTrace();  
        	return false;
        }
	}	
	
	
	private void print(String msg){			
		if(debug) System.out.println("["+this.getClass().getSimpleName()+"] "+msg);
	}
}