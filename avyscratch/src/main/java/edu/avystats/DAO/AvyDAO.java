package edu.avystats.DAO;


import java.security.interfaces.RSAKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;



import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class AvyDAO {

	private JdbcTemplate jdbctemplate;
	
	public AvyDAO(){
		
	}
	
	public void setDataSource(DriverManagerDataSource datasource){
		jdbctemplate=new JdbcTemplate(datasource);
	}
	
	public void calcMeans(){
		
	}
	
	
	public void postObservation(double latitude,double longitude,int month,int day,int year){
		//post an observation
		String query="INSERT INTO avy_preprod.observations (latitude,longitude,month,day,year) VALUES('"+latitude+"','"+longitude+"','"+month+"','"+day+"','"+year+"')";
		this.jdbctemplate.execute(query);
	}
	
	public List<Coord> findMapCoords(double latitude,double longitude){
		return this.jdbctemplate.query("SELECT t1.location,t2.latitude,t2.longitude FROM avy_preprod.avalanches as t1 INNER JOIN avy_preprod.location_lu as t2 ON t1.location_id_lu=t2.location_id WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(t2.latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(t2.latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(t2.longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120", new CoordMapper());		
	}
	
	
	public List<Observation> findObservations(String type,String current,String latitude, String longitude){
		if(latitude != null && longitude != null){
			String q="SELECT distinct on(latitude,longitude,month,day,year) rownum,longitude,latitude,month,day,year FROM (SELECT row_number() over (order by latitude nulls last) as rownum,* FROM avy_preprod.observations WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120) as q1 WHERE rownum >"+(Integer.parseInt(current)*7)+" LIMIT 7";
			System.out.println(q);
			return this.jdbctemplate.query(q, new ObsMapper());
		}
		return null;
	}
	
	public List<Hotspot> findHotSpot(String type,String current,String latitude, String longitude){
		if(latitude != null && longitude != null){
			return this.jdbctemplate.query("SELECT * FROM (SELECT distinct on(longitude,latitude) row_number() over (order by latitude nulls last) as rownum,longitude,latitude,freq FROM avy_preprod.hotspots as q2 WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(q2.latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(q2.latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120 ORDER BY latitude,longitude) as q2  WHERE rownum > "+(Integer.parseInt(current)*15)+" LIMIT 15", new HotSpotMapper());
		}
		return null;
	}
	
	public List<Weather> findWeather(String type,String current,String latitude, String longitude){
		if(latitude != null && longitude != null){
			return this.jdbctemplate.query("SELECT * FROM (SELECT row_number() over (order by avalanche_id nulls last) as rownum,* FROM (SELECT t1.avalanche_id,t1.location_id_lu,t1.month,t1.day,t1.year,t1.latitude,t1.longitude,t2.temperature,t2.dewpoint,t2.sealevelpressure,t2.visibility,t2.maxtemperature,t2.mintemperature FROM (SELECT sq1.avalanche_id,sq1.month,sq1.day,sq1.year,sq1.location_id_lu,sq2.latitude,sq2.longitude FROM avy_preprod.avalanches as sq1 INNER JOIN avy_preprod.location_lu as sq2 ON sq1.location_id_lu=sq2.location_id) as t1 INNER JOIN (SELECT q.avalanche_id,AVG(q.temperature) as temperature, AVG(q.dewpoint) as dewpoint, AVG (sealevelpressure) as sealevelpressure, AVG(visibility) as visibility, MAX(maxtemperature) as maxtemperature, MIN(mintemperature) as mintemperature FROM (SELECT * FROM avy_preprod.avalanche_weather as t1 INNER JOIN avy_preprod.weather as t2 ON t1.year=t2.year AND t1.day=t2.day AND t1.month=t2.month and t1.weather_site_id=t2.site_id ORDER BY avalanche_id) as q GROUP BY avalanche_id)as t2 ON t1.avalanche_id=t2.avalanche_id) as oq WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120) as q1 WHERE rownum >"+(Integer.parseInt(current)*15)+" LIMIT 15", new WeatherMapper());
		}
		return null;
	}
	
	public List<Storm> findStorm(String type,String current,String latitude, String longitude){
		if(latitude != null && longitude != null){
			return this.jdbctemplate.query("SELECT * FROM (SELECT row_number() over (order by avalanche_id nulls last) as rownum,* FROM (SELECT t1.avalanche_id,MAX(t1.location_id_lu) as location_id_lu,MAX(t1.longitude) as longitude,MAX(t1.latitude) as latitude,MAX(t1.month) as month,MAX(t1.day) as day,MAX(t1.year) as year,AVG(t2.precipitation) as precipitation,every(t2.rain) as rain,every(t2.snow) as snow,every(t2.thunder) as thunder,every(t2.fog) as fog FROM (SELECT sq1.avalanche_id,sq1.location_id_lu,sq2.latitude,sq2.longitude,sq1.month,sq1.day,sq1.year FROM avy_preprod.avalanches as sq1 INNER JOIN avy_preprod.location_lu as sq2 ON sq1.location_id_lu=sq2.location_id) as t1 INNER JOIN (SELECT q1.avalanche_id,q2.precipitation,q2.rain,q2.snow,q2.thunder,q2.fog,q2.hail,q2.tornado,q2.month,q2.day,q2.year FROM avy_preprod.avalanche_storm as q1 INNER JOIN avy_preprod.storms as q2 ON q1.storm_site_id=q2.site_id) as t2 ON t1.avalanche_id=t2.avalanche_id WHERE t1.year=t2.year AND t1.month=t2.month AND t1.day=t2.day GROUP BY t1.avalanche_id) as oq WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120) as q1 WHERE rownum > "+(Integer.parseInt(current)*15)+" LIMIT 15 ", new StormMapper());
		}
		return null;
	}
	
	public List<Snow> findSnow(String type,String current,String latitude, String longitude){
		if(latitude != null && longitude != null){
			return this.jdbctemplate.query("SELECT * FROM (SELECT row_number() over (order by t1.avalanche_id nulls last) as rownum,* FROM (SELECT avalanche_id,location_id_lu FROM avy_preprod.avalanches) as t1 INNER JOIN (SELECT s1.avalanche_id,MAX(s1.year) as year,MAX(s1.month) as month,MAX(s1.day) as day,MAX(s2.latitude) as latitude,MAX(s2.longitude) as longitude,AVG(s2.waterequivalent) as waterequivalent,AVG(s2.snowdepth) as snowdepth FROM avy_preprod.avalanche_snowpack as s1 INNER JOIN (SELECT ss1.site_id,ss1.month,ss1.day,ss1.year,ss1.waterequivalent,ss1.snowdepth,ss2.latitude,ss2.longitude FROM avy_preprod.snowpack as ss1 INNER JOIN avy_preprod.snotels as ss2 ON ss1.site_id=ss2.site_id WHERE snowdepth >0) as s2 ON s1.year=s2.year WHERE s1.month=s2.month AND s1.day=s2.day AND s1.snotel_site_id=s2.site_id GROUP BY s1.avalanche_id) as t2 ON t1.avalanche_id=t2.avalanche_id ORDER BY t2.avalanche_id) as rq WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(rq.latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(rq.latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(rq.longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120 AND rownum> "+(Integer.parseInt(current)*15)+" LIMIT 15", new SnowMapper());
		}
		return null;
	}
	
	public List<Avalanche> find(String type,String current,String latitude, String longitude){
		//get next page
		if(latitude != null && longitude != null){
			String q="SELECT * FROM (SELECT row_number() over (order by avalanche_id nulls last) as rownum,* FROM avy_preprod.avalanches as q1 INNER JOIN avy_preprod.location_lu as q2 ON q1.location_id_lu=q2.location_id WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(q2.latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(q2.latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120) as q1 WHERE rownum >"+(Integer.parseInt(current)*7)+" LIMIT 7";
			System.out.println(q);
			return this.jdbctemplate.query(q, new AvyMapper());
		}
		return null;
	}
	
	public List<Avalanche> findAll(double latitude, double longitude){
		//return homepage table
		return this.jdbctemplate.query("SELECT * FROM avy_preprod.avalanches as q1 INNER JOIN avy_preprod.location_lu as q2 ON q1.location_id_lu=q2.location_id WHERE (acos(sin(abs("+latitude+")*pi()/180)*sin(cast(q2.latitude as DOUBLE PRECISION)*pi()/180)+cos(abs("+latitude+")*pi()/180)*cos(cast(q2.latitude as DOUBLE PRECISION)*pi()/180)*cos(abs("+longitude+"*pi()/180)-(cast(longitude AS DOUBLE PRECISION)*pi()/180))))*6381<=120 LIMIT 7", new AvyMapper());
	}
	
	public List<MeansVar> findKMeans(String o1,String o2,double latitude,double longitude){
		String query="SELECT ";
		
		query+="cast(t1."+o1+" as DOUBLE PRECISION) as "+o1+", cast(t1."+o2+" as DOUBLE PRECISION) as "+o2+", t2.location_id FROM avy_preprod.avalanches as t1 INNER JOIN avy_preprod.location_lu as t2 ON t1.location_id_lu=t2.location_id WHERE t1."+o1+" IS NOT NULL AND t1."+o2+" IS NOT NULL ";
		
		System.out.println(query);
		
		return this.jdbctemplate.query(query, new MeansMapper());
	}
	
	public static final class MeansMapper implements RowMapper<MeansVar>{

		@Override
		public MeansVar mapRow(ResultSet rs, int size) throws SQLException {
			// TODO Auto-generated method stub
			MeansVar mv=new MeansVar();
			mv.setO1(rs.getMetaData().getColumnName(1).toUpperCase());
			mv.setO2(rs.getMetaData().getColumnName(2).toUpperCase());
			mv.setMv1(rs.getDouble(1));
			mv.setMv2(rs.getDouble(2));
			mv.setLocationID(rs.getInt("location_id"));
			return mv;
		}
	
	}
	
	public static final class ObsMapper implements RowMapper<Observation>{
		public Observation mapRow(ResultSet rs, int rowNum) throws SQLException {
			Observation obs=new Observation();
			obs.setDay(rs.getInt("day"));
			obs.setMonth(rs.getInt("month"));
			obs.setYear(rs.getInt("year"));
			obs.setLatitude(rs.getDouble("latitude"));
			obs.setLongitude(rs.getDouble("longitude"));
			return obs;
		}
	}
	
	private static final class AvyMapper implements RowMapper<Avalanche> {
		public Avalanche mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Avalanche avy = new Avalanche();
	        
	        //set elements
	        avy.setAngle(rs.getString("angle"));
	        avy.setAspect(rs.getString("aspect"));
	        avy.setDay(rs.getString("day"));
	        avy.setDepth(rs.getString("depth"));
	        avy.setDfactor(rs.getString("dfactor"));
	        avy.setElevation(rs.getString("elevation"));
	        avy.setHour(rs.getString("hour"));
	        avy.setLocation(rs.getString("location"));
	        avy.setLocationID(rs.getString("location_id_lu"));
	        avy.setMinute(rs.getString("minute"));
	        avy.setMonth(rs.getString("month"));
	        avy.setPathsize(rs.getString("pathsize"));
	        avy.setRfactor(rs.getString("rfactor"));
	        avy.setState(rs.getString("state"));
	        avy.setTime(rs.getString("time"));
	        avy.setTrigger(rs.getString("trigger_lu"));
	        avy.setWeaklayer(rs.getString("weaklayer_lu"));
	        avy.setWidth(rs.getString("width"));
	        avy.setYear(rs.getString("year"));
	        
	        return avy;
	    }
	}
	
	
	private static final class HotSpotMapper implements RowMapper<Hotspot> {
		public Hotspot mapRow(ResultSet rs, int rowNum) throws SQLException {
			Hotspot hs=new Hotspot();
			hs.setFrequency(rs.getInt("freq"));
			hs.setLatitude(rs.getDouble("latitude"));
			hs.setLongitude(rs.getDouble("longitude"));
			return hs;
		}
	}
	
	private static final class SnowMapper implements RowMapper<Snow> {
		public Snow mapRow(ResultSet rs, int rowNum) throws SQLException {
			Snow snow=new Snow();
			
			snow.setAvalancheID(rs.getInt("avalanche_id"));
			snow.setDay(rs.getInt("day"));
			snow.setMonth(rs.getInt("month"));
			snow.setLocationID(rs.getInt("location_id_lu"));
			snow.setSnowdepth(rs.getInt("snowdepth"));
			snow.setWaterequivalent(rs.getDouble("waterequivalent"));
			
			return snow;
			
		}
	}
	
	private static final class WeatherMapper implements RowMapper<Weather>{
		public Weather mapRow(ResultSet rs, int rowNum) throws SQLException {
			Weather weather=new Weather();
			weather.setLocationID(rs.getInt("location_id_lu"));
			weather.setAvalancheID(rs.getInt("avalanche_id"));
			weather.setDay(rs.getInt("day"));
			weather.setMonth(rs.getInt("month"));
			weather.setYear(rs.getInt("year"));
			weather.setSealevelpressure(rs.getDouble("sealevelpressure"));
			weather.setTemperature(rs.getDouble("temperature"));
			weather.setVisibility(rs.getDouble("visibility"));
			weather.setYear(rs.getInt("year"));
			return weather;
			
		}
	}
	
	private static final class StormMapper implements RowMapper<Storm>{
		public Storm mapRow(ResultSet rs, int rowNum) throws SQLException {
			Storm storm=new Storm();
			
			storm.setAvalancheID(rs.getInt("avalanche_id"));
			storm.setLocationID(rs.getInt("location_id_lu"));
			storm.setDay(rs.getInt("day"));
			storm.setMonth(rs.getInt("month"));
			storm.setYear(rs.getInt("year"));
			storm.setFog(rs.getBoolean("fog"));
			storm.setRain(rs.getBoolean("rain"));
			storm.setSnow(rs.getBoolean("snow"));
			storm.setThunder(rs.getBoolean("thunder"));
			storm.setPrecipitation(rs.getDouble("precipitation"));
			
			return storm;
		}
	}
	
	private static final class CoordMapper implements RowMapper<Coord>{
		public Coord mapRow(ResultSet rs, int rowNum) throws SQLException {
			Coord coord=new Coord();
			coord.setLatitude(rs.getDouble("latitude"));
			coord.setLongitude(rs.getDouble("longitude"));
			coord.setLocation(rs.getString("location"));
			
			return coord;
			
		}
	}
}