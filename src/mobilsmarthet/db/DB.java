package mobilsmarthet.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;


import mobilsmarthet.bluetooth.SerializableSensor;
import mobilsmarthet.Server.Sensors;





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
	
	public Map<Integer,Double> getLastSensorValue(int sensor){
		Sensors s = Sensors.match(sensor);
		if(s == null) return null;
		String query = "SELECT * FROM "+sensor+"LIMIT 1";
		return getSensorValue(query);
	}
	public ArrayList<SerializableSensor> getSensorsValue(int time){		
		ArrayList<SerializableSensor> list = new ArrayList<SerializableSensor>();
		for(Sensors s : Sensors.values()){
			list.add(
				new SerializableSensor(
							getSensorValue(s.getId(),time),
							s.getId()							
						)					
			);
		}
		return list;
	}
	
	public Map<Integer,Double> getSensorValue(int sensor, int time) throws NullPointerException{
		Sensors s = Sensors.match(sensor);
		if(s == null) return null;
		String query = "SELECT * FROM "+s.getName()+" WHERE `time` > "+time+" ORDER BY time";
		return getSensorValue(query);
	}
	
	private Map<Integer,Double> getSensorValue(String query){
		Map<Integer,Double> data = new HashMap<Integer,Double>();
		try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
            	data.put(rs.getInt(1), rs.getDouble(2));
            }
            rs.close();
        	st.close();        	
        }catch(SQLException ex) {
        	ex.printStackTrace();
        }
		return data;
	}
	public void addSensorvalue(String sensor, double value){

		String query = "INSERT INTO "+sensor+" (time,value)"+
		               " VALUES ('"+Instant.now().getEpochSecond()+"','"+value+"')";
		try {
			Statement st = con.createStatement();
            st.executeUpdate(query);
            st.close();
            print("Inserted into "+ sensor);
        }catch(SQLException ex) {
        	ex.printStackTrace();           
        }
	}
	
	public void addSensorvalue(int sensor, double value){
		Sensors s = Sensors.match(sensor);
		if(s == null) return;
		addSensorvalue(s.getName(), value);
	}
	
	private void print(String msg){			
		if(debug) System.out.println("["+this.getClass().getSimpleName()+"] "+msg);
	}
}
