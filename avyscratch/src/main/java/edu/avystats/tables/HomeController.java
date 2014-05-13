package edu.avystats.tables;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.maxmind.geoip2.*;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import edu.avystats.DAO.Avalanche;
import edu.avystats.DAO.AvyDAO;
import edu.avystats.geoservices.GeoServicesDB;
import edu.avystats.geoservices.GeoDBPath;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes());
		String ip=requestAttributes.getRequest().getRemoteAddr();
		
		//my ip should be all zeros for local host at the moment and so I need to change it 
		if(ip.contains("0:0:0:0:0:0:0:1")||ip.contains("127.0.0.1")){
			ip="98.245.19.245";
		}
		
		
	
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("file:C:/Users/andy/Documents/DB/avy.xml");
		
		GeoDBPath gpath=(GeoDBPath) ctx.getBean("GPS");
		
		GeoServicesDB geodb=GeoServicesDB.getInstance(gpath.getFpath());
		ArrayList<Double> coords=geodb.getGPS(ip);
		
		double latitude=coords.get(0);
		double longitude=coords.get(1);
		
		AvyDAO avyobjs=(AvyDAO) ctx.getBean("AvyDAO");
				
		
		List<Avalanche> avs=avyobjs.findAll(latitude,longitude);
		String outhtml="<table>";
		outhtml+="<tr><th>Location ID</th><th>Location</th><th>Day</th><th>Month</th><th>Year</th><th>Angle</th><th>Aspect</th><th>Elevation</th><th>Trigger</th><th>Weak Layer</th><th>RFactor</th><th>DFactor</th><th>Depth</th><th>Path Size</th><th>Width</th></tr>";

		int i=0;
		for(Avalanche av: avs){
			if((i%2)==0){
				outhtml+="<tr style='background-color:DCDCDC;border-radius:10px;margin:0;border:0;padding:0;'><td style='border:0;margin:0;border:0;padding:0;'>"+av.getLocationID()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getLocation()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getDay()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getMonth()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getYear()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getAngle()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getAspect()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getElevation()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getTrigger()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getWeaklayer()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getRfactor()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getDfactor()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getDepth()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getPathsize()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getWidth()+"</td></tr>";
			}
			else{
				outhtml+="<tr style='margin:0;border:0;border-radius:10px;padding:0;'><td style='border:0;margin:0;border:0;padding:0;'>"+av.getLocationID()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getLocation()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getDay()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getMonth()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getYear()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getAngle()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getAspect()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getElevation()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getTrigger()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getWeaklayer()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getRfactor()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getDfactor()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getDepth()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getPathsize()+"</td><td style='border:0;margin:0;border:0;padding:0;'>"+av.getWidth()+"</td></tr>";
			}
			i++;
		}
		
		model.addAttribute("avyTable",outhtml);
		
		
		outhtml+="</table>";
		
		model.addAttribute("avyTable",outhtml.replaceAll("null",""));
		
		
		return "home";
	}

}
