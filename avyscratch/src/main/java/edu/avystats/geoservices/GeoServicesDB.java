package edu.avystats.geoservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;



import javax.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

public class GeoServicesDB {

	private static GeoServicesDB db;
	private DatabaseReader reader;
	
	//home point is rmnp -- zerkel,front range, rmnp, berthoud, jones, byers,gilpin (includes deadliest and most iconic areas)
	private double longitude=-105.767212;
	private double latitude=40.326864;
	
	protected GeoServicesDB(String indat){
		URL file=getClass().getClassLoader().getResource(indat);
		
		File f=new File(file.toString().replaceAll("file:|\\s", ""));
		

		if(f.exists()){
			try {
				
				reader=new DatabaseReader.Builder(new FileInputStream(f)).build();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try{
				throw new FileNotFoundException("Cannot Find DB File");
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}
	}
	
	public static GeoServicesDB getInstance(String dat){
		
		if(db == null){
			db=new GeoServicesDB(dat);
		}
		
		return db;
	}
	
	
	public synchronized ArrayList<Double> getGPS(String ip){
		
		//home point is rmnp, instantiate
		ArrayList<Double> gps=new ArrayList<Double>();
		longitude=-105.767212;
		latitude=40.326864;
		
		//get gps coords
		try {
			CityResponse response=reader.city(InetAddress.getByName(ip));
			longitude=response.getLocation().getLongitude();
			latitude=response.getLocation().getLatitude();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//add to output
		gps.add(latitude);
		gps.add(longitude);
		
		//return
		return gps;
		
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	
}
