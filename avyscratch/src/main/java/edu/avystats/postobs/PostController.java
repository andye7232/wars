package edu.avystats.postobs;

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

import com.google.gson.Gson;

import edu.avystats.DAO.Avalanche;
import edu.avystats.DAO.AvyDAO;

@Controller
@RequestMapping("/postobs")
public class PostController {
		
		@RequestMapping(value="/postobs",method = RequestMethod.POST)
		public ResponseEntity<String> newAnswer(
				@RequestParam( value="latitude", required=true ) double latitude
		        , @RequestParam( value="longitude", required=true ) double longitude
		        , @RequestParam( value="month", required=true ) int month
		        , @RequestParam( value="day", required=true ) int day
		        , @RequestParam( value="year", required=true ) int year
		        ){
			
				HttpHeaders headers=new HttpHeaders();
				headers.set("Content-Type","text/html; charset=utf-8");
				ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("file:C:/Users/andy/Documents/DB/avy.xml");
				
				AvyDAO avyobjs=(AvyDAO) ctx.getBean("AvyDAO");
			
				avyobjs.postObservation(latitude,longitude,month,day,year);			
				return new ResponseEntity<String>("success",headers,HttpStatus.CREATED);
		}
		
	}