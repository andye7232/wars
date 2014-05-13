package edu.avystats.geoip;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

import edu.avystats.DAO.Avalanche;
import edu.avystats.DAO.AvyDAO;
import edu.avystats.DAO.Coord;
import edu.avystats.geoservices.GeoDBPath;
import edu.avystats.geoservices.GeoServicesDB;

@Controller
@RequestMapping("/geoip")
public class GeoIpController{
	
	private ResponseEntity<String> createJsonResponse(Object o){
		HttpHeaders headers=new HttpHeaders();
		headers.set("Content-Type","application/json");
		Gson gson=new Gson();
		String json = gson.toJson(o);
		return new ResponseEntity<String>(json,headers,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/geoip",method = RequestMethod.POST)
	public ResponseEntity<String> newAnswer(@RequestParam(value="getip",required=true) Boolean getip){
		
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("file:C:/Users/andy/Documents/DB/avy.xml");

		ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes());
		String ip=requestAttributes.getRequest().getRemoteAddr();
		
		//my ip should be all zeros for local host at the moment and so I need to change it 
		if(ip.contains("0:0:0:0:0:0:0:1")||ip.contains("127.0.0.1")){
			ip="98.245.19.245";
		}
		
		if(getip==true){
			
			GeoDBPath gpath=(GeoDBPath) ctx.getBean("GPS");
			
			GeoServicesDB geodb=GeoServicesDB.getInstance(gpath.getFpath());
			ArrayList<Double> coords=geodb.getGPS(ip);
			
			double latitude=coords.get(0);
			double longitude=coords.get(1);
			
			List<Coord> answer=new ArrayList<Coord>();
			
			Coord c=new Coord();
			
			c.setLatitude(latitude);
			c.setLongitude(longitude);
			c.setLocation("You are Here");
			c.setLocationid(0);
			
			answer.add(c);
			
			return createJsonResponse(answer);
		}
	
		//return null since nothing was found
		return null;
	}
}
	
