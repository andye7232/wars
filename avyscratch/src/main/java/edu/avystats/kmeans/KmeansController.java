package edu.avystats.kmeans;

import java.util.ArrayList;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import edu.avystats.DAO.AvyDAO;
import edu.avystats.DAO.MeansVar;

@Controller
@RequestMapping("/kmeans")
public class KmeansController {

	private ResponseEntity<String> createJsonResponse(Object o){
		HttpHeaders headers=new HttpHeaders();
		headers.set("Content-Type","application/json");
		Gson gson=new Gson();
		String json = gson.toJson(o);
		return new ResponseEntity<String>(json,headers,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/kmeans",method = RequestMethod.POST)
	public ResponseEntity<String> newAnswer(
			@RequestParam(value="o1",required=true) String o1
			,@RequestParam(value="o2",required=true) String o2
			,@RequestParam( value="latitude", required=true ) double latitude
	        ,@RequestParam( value="longitude", required=true ) double longitude){
		
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("file:C:/Users/andy/Documents/DB/avy.xml");

		AvyDAO dao=(AvyDAO) ctx.getBean("AvyDAO");
		
		ArrayList<MeansVar> ktest=new ArrayList<MeansVar>();
		
		
		return createJsonResponse(ktest);
	
	}
		
	
}
