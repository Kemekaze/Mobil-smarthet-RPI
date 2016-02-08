package db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import api.*;

public class DB {
	private static List<String> files = new ArrayList<String>();
	private static final String dir = System.getProperty("user.home")+"/.dat065/";
	
	public static final int SUCCESS = 1;	
	private static DB db = null;
	
	private DB(){
		db = this;
	}
	
	public static DB get(){
		return (db == null)? new DB():db; 
	}

	public void addSensors(List<Sensor> sensors) throws IOException{		
		for(Sensor s : sensors){
				addSensor(s);
		}
	}
	public void addSensor(Sensor s) throws IOException{
		File f = new File(dir+s.getClassName()+".db");
		if(!f.exists()){
			if(f.getParentFile().mkdirs())
				f.createNewFile();
		}
		System.out.println(dir+s.getClassName()+".db");
		System.out.println(f.getAbsolutePath());
		files.add(f.getAbsolutePath());			
	}
	public List<Integer> read(Sensor s) throws IOException{
		if(files.size() == 0) return null;
		String path;
		if(files.contains(dir+s.getClassName()+".db"))
			path =dir+s.getClassName()+".db";
		else
			return null;
		ArrayList<Integer> data = new ArrayList<Integer>();
		
		for (String line : Files.readAllLines(Paths.get(path))) {
			data.add(Integer.parseInt(line.split(":")[0]));
		}
		return data;
	}
	public void write(Sensor s, String data) throws IOException{
		String path;
		if(files.contains(dir+s.getClassName()+".db"))
			path =dir+s.getClassName()+".db";
		else
			return;
		Files.write(Paths.get(path), data.getBytes(), StandardOpenOption.APPEND);		
	}

}
