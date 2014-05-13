package edu.avystats.expand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.stream.*;

import edu.avystats.DAO.Avalanche;
import edu.avystats.DAO.AvyDAO;
import edu.avystats.DAO.Coord;
import edu.avystats.DAO.Hotspot;
import edu.avystats.DAO.Observation;
import edu.avystats.DAO.Snow;
import edu.avystats.DAO.Storm;
import edu.avystats.DAO.Weather;

@Controller
@RequestMapping("/expand")
public class ExpandController {
	

	private ResponseEntity<String> createJsonResponse(Object o){
		HttpHeaders headers=new HttpHeaders();
		headers.set("Content-Type","application/json");
		Gson gson=new Gson();
		String json = gson.toJson(o);
		return new ResponseEntity<String>(json,headers,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/expand",method = RequestMethod.POST)
	public ResponseEntity<String> newAnswer(@RequestParam(value="type",required=true) String type
			,@RequestParam( value="current", required=true ) String current
	        , @RequestParam( value="latitude", required=true ) String latitude
	        , @RequestParam( value="longitude", required=true ) String longitude
	        ){
		
			ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("file:C:/Users/andy/Documents/DB/avy.xml");
		
			AvyDAO avyobjs=(AvyDAO) ctx.getBean("AvyDAO");
		
			if(latitude != null && longitude != null){
				if(latitude.trim().length() ==0 || latitude.trim().length()==0){
					System.out.println("A GPS coordinate was null.");
					return null;
					
				}
			}
			
			if(type != null){
				//get data from avydao
				if(type.compareTo("addavy")==0){
					List<Avalanche> answer=new ArrayList<Avalanche>();
					answer=avyobjs.find(type,current,latitude,longitude);
					return createJsonResponse(answer);
				}
				else if(type.compareTo("mapcoord")==0){
					if(latitude != null && longitude != null){
						List<Coord> answer=new ArrayList<Coord>();
						answer=avyobjs.findMapCoords(Double.parseDouble(latitude),Double.parseDouble(longitude));
						return createJsonResponse(answer);
					}
				}
				else if(type.compareTo("weather")==0){
					List<Weather> answer=new ArrayList<Weather>();
					answer=avyobjs.findWeather(type, current, latitude, longitude);
					return createJsonResponse(answer);
				}
				else if(type.compareTo("observations")==0){
					List<Observation> answer=new ArrayList<Observation>();
					answer=avyobjs.findObservations(type, current, latitude, longitude);
					return createJsonResponse(answer);
				}
				else if(type.compareTo("snowpack")==0){
					List<Snow> answer=new ArrayList<Snow>();
					answer=avyobjs.findSnow(type, current, latitude, longitude);
					return createJsonResponse(answer);
				}	
				else if(type.compareTo("observations")==0){
					List<Observation> answer=new ArrayList<Observation>();
					answer=avyobjs.findObservations(type, current, latitude, longitude);
					return createJsonResponse(answer);
				}
				else if(type.compareTo("hotspots")==0){
					List<Hotspot> answer=new ArrayList<Hotspot>();
					answer=avyobjs.findHotSpot(type, current, latitude, longitude);
					return createJsonResponse(answer);
				}
				else if(type.compareTo("storm")==0){
					List<Storm> answer=new ArrayList<Storm>();
					answer=avyobjs.findStorm(type, current, latitude, longitude);
					return createJsonResponse(answer);
				}
			}
				
				
			//return null since nothing was found
			return null;

	}
	
}