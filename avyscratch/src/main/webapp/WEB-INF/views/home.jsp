<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
	<head profile="http://www.w3.org/2005/10/profile">
	<link rel="icon" type="image/png" href="http://powdernow.com/img/icons/favicon.ico">
		<title>Avalanche</title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
    <!-- Bootstrap powdernow/test/css/bootstrap-theme.css-->
    <link href="http://www.powdernow.com/test/css/bootstrap.min.css" rel="stylesheet">
    <!--Custom css -->
     <link href="http://www.powdernow.com/test/css/customcss.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
    	var current=0;
    	var latitude=null;
    	var longitude=null;   
    	var tabletype="addavy";
    	var response=true;
    	var formvis=false;
    	var markers=null;
    	var statcur=0;
    	
    	var maptype="map";
    	
    	
    	//if getting the gps coordinates produces an error, throw this
    	function positionError(){
    		alert("Could Not Get Latitude or Longitude or they are null");
    	}
    	
    	//get the gps coordinate position
    	function getPosition(position){
    		latitude=position.coords.latitude;
    		longitude=position.coords.longitude;
    	}
    	
 
    	
    	//returns table text from returned json data
    	function getJsonTables(data){
    		var rows=null;
			var table="<table><tr>";
			
			var keys=[];
			if(data[0] != undefined){
				for(var key in data[0]){
					table+="<th style='width:10px;padding:5px;'>"+key.toUpperCase()+"</th>";
					keys.push(key);
				}
				table+="</tr>"
				var k=null;
				for(var i=0;i<data.length;i++){
					rows=(rows==null)?"<tr style='background-color:DCDCDC;border-radius:10px;margin:0;border:0;padding:0;' >":rows+"<tr  style='background-color:DCDCDC;border-radius:10px;margin:0;border:0;padding:0;' >";
					for(var j=0;j<keys.length;j++){
						k=keys[j];
						if(data[i][k] != undefined){
							rows+="<td style='width:10px;padding:5px;' id='"+k+"'>"+data[i][k]+"</td>";
						}
						else{
							rows+="<td style='width:10px;padding:5px;'></td>"
						}
					}		
					rows+"</tr>";
					j++;
				}
				table+=rows+"</table>";
				return table;
			}
			else{
				return "<p style='color:red;'>No Data Available. Please Help Encourage More Open Acess!</p>"
			}
			
			return null;
    	}
    	
    	
    	//writes the main table
    	function writeMainTable(){
    		response=true;
    		//get data from ajax post to spring
			$.post("http://localhost:8080/tables/expand",{
				type:tabletype,
				current:current,
				latitude:latitude,
				longitude:longitude
			},
			function(data,status){
				var tabletext;
				if(data != null){
					if(data != 'null' && data != undefined && data.length>0){
						tabletext=getJsonTables(data);
						document.getElementById('maintable').innerHTML=tabletext;
					}
					else{
						response=false;
					}
				}
				else{
					response=false;
				}
			});
    	}
    	
    	//change a table
    	function changeTable(intype){
    		if(intype != tabletype){
    			tabletype=intype;
    			var oldcur=current;
    			var oldtype=tabletype;
    			var rows=null;
				navigator.geolocation.getCurrentPosition(getPosition, positionError);
				current=0;
				writeMainTable();
 	  		}
    	}
		
    	//get the next position
		function Next(){
			//iterate --> number of results is automatically 20 for UI
			var rows=null;
			
			current+=1;
			repsonse=true;
			rows=writeMainTable();
			
			//if the response was null set the current back 1
			if(response==false){
				current=current-1;
			}
			
		}
		
    	//get the previous tables
		function Previous(){
			
			
			if(current>0){
				//iterate
				current=current-1;
				var toofar=false;
				var tabletext=null;	
				if(current<0){
					current=0;
					toofar=true;
				}
				else{
					tabletext=writeMainTable();
					
					if(toofar==false && response==false){
						current++;
					}
				}
			}
			
		}
		
    	//reset the latitude and longitude and tables based on that call
		function resetLatLng(lat,lng){
			latitude=lat;
			longitude=lng;
			
			//reset current
			current=1;
			
			getMarkers();
			
			//rewrite main table
			tabletype="addavy";
			writeMainTable();
			
			
			if(response==true){
				document.getElementById('statstable').innerHTML=texttable;
			}
			
			
		}

    	//show form
		function showForm(){
    		
    		if(document.getElementById('submitform').value == ''  || document.getElementById('submitform').value == null || formvis==false){
    			var form="<form name='obsform' id='obsform'><p><input type='text' id='latitude' name='latitude' size='10' value='latitude'/>&nbsp;<input type='text' id='longitude' name='longitude' size='10' value='longitude'/></p><p><input type='text' id='month' name='month' size='2' value='mm'/><input type='text' id='day' name='day' size='2' value='dd'/><input type='text' id='year' name='year' size='4' value='yyyy'/></p><p></form><input type='button' value='Submit Observation' onClick='submitObservation()'/>"
    			formvis=true;
    			$('#submitform').append(form);
    		}
    	}
		
		function getStats(){
    		
    		var optsstr=$("#statsform").serialize();
    		optsstr+="&latitude="+latitude+"&longitude="+longitude;
    
    		$.post("http://localhost:8080/tables/kmeans",optsstr,function(data,status){
    			alert(status);
    			/*
    			if(status=="success" && data != 'undefined' && data != null){
    				for(var i=0){
    					tabletext=(tabletext==null)?"<p>Cluster "+i+"</p><p>getJsonTables(data[i])</p>:tabletext+"<br/><p>Cluster "+i+"</p><p>"+getJsonTables(data[i])+"</p>";
    				}
    				document.getElementById('statstable').innerHTML=
    			}
    			*/
    		}); 
    		
    	}
    	
    	//submit an observation
    	function submitObservation(){
    		
    		if(formvis == true){
    			formvis=false;
    			var datastr=$('#obsform').serialize();
    			//submit the observation
    			$.post("http://localhost:8080/tables/postobs",datastr);  			
    		
    			//if the type is observations
    			if(tabletype=='observations'){
    				current=0;
    				writeMainTable();
    			}
    		}
    		document.getElementById('submitform').innerHTML="";
    		
    	}
    	
    	
    	//change the map type
		function changeMap(intype){
    		
    		if(markers == null){
    			$.post("http://localhost:8080/tables/expand",{
    				type:"mapcoord",
    				current:current,
    				latitude:latitude,
    				longitude:longitude
    			},
    			function(data,status){
    				markers=data;
    					
    			});
    		}
    		
    		
    		if(markers != null){
				if(intype=="heat" && maptype != "heat"){
					maptype="heat";
					initializeHeat(markers,latitude,longitude);
				}
				else if(intype=="map" && maptype != "map"){
					maptype="map";
					initialize(markers,latitude,longitude);
					setPos(latitude,longitude);
				}
    		}
		}
 
    	
    	function getMarkers(){
  
    		$.post("http://localhost:8080/tables/expand",{
				type:"mapcoord",
				current:current,
				latitude:latitude,
				longitude:longitude
			},
			function(data,status){
				var tabletext;
				if(data != null){
					if(data != 'null' && data != undefined && data.length>0){
						markers=data;
						if(maptype=="map"){
							initialize(data,latitude,longitude);
						}
						else if(maptype=="heat"){
							initializeHeat(data,latitude,longitude);
						}
						
					}
				}
					
			});
    	}
    	
    
    	
    	$(document).ready(function(){
    		$.post("http://localhost:8080/tables/geoip",{
				getip:true,
			},
			function(data,status){
				var tabletext;
				if(data != null){
					if(data != 'null' && data != undefined && data.length>0){
						
						latitude=data[0]["latitude"];
						longitude=data[0]["longitude"];
						getMarkers();
					}
					else{
						navigator.geolocation.getCurrentPosition(getPosition, positionError);
						alert("Cannot Get Your IP. Attempting a Google Call!");
						getMarkers()
					}
				}
				else{
					navigator.geolocation.getCurrentPosition(getPosition, positionError);
					alert("Cannot Get Your IP. Attempting a Google Call!");
					getMarkers();
				}
					
			});
    	});
	</script>
   </head>
	<body>
		<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="javascript:void(0)" onClick="showForm()">Submit Report</a></li>
        <li><a href="#contact">Contact us</a></li>
        <li><a href="http://www.powdernow.com/credits">Workload</a></li>
      </ul>
     </li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
<div class="page-header text-center"><h1>Avalanche</h1></div>
<div id="submitform" name="submitform" style="left:0px;top:0px;z-index:-1;">
</div>
<div class="text-center"><i>WARNING:</i> Do to Our Sources, this site only works in North America</div>
<div id="test"></div>
<div class="container-fluid">
	<div style="margin-left:3%;"><input type="button" class="btn-primary" name="map" id="map" value="Map" onClick="changeMap('map')"><input type="button" class="btn-primary" name="Heat" id="Heat" value="Heat" onClick="changeMap('heat')"></div>
	<div class="row maparea">
		<div class="col-md-5 container-fluid map">
			<div class="map" id="regularMap">
					<div id="map-canvas"></div>
			</div>	
		</div>
		<div class="col-md-6">
			<!-- Nav tabs -->
<ul class="nav nav-tabs">
  <li class="active"><a href="javascript:void(0)" onClick="changeTable('addavy')" data-toggle="tab">All Avalanches</a></li>
  <li><a href="javascript:void(0)" onClick="changeTable('observations')" data-toggle="tab">Observations</a></li>
  <li><a href="javscript:void(0)" onClick="changeTable('weather')" data-toggle="tab">Weather</a></li>
  <li><a href="javascript:void(0)" onClick="changeTable('snowpack')" data-toggle="tab">Snowpack</a></li>
  <li><a href="javascript:void(0)" onClick="changeTable('storm')" data-toggle="tab">Storm</a></li>
  <li><a href="javascript:void(0)" onClick="changeTable('hotspots')" data-toggle="tab">Hotspots</a></li>
</ul>
	<div id="maintable" name="maintable">
	 ${avyTable}
	</div>
	<div id="scrollbuttons" name="scrollbuttons">
		<center>
			<input type="button" value="Previous" onClick="Previous()"/><input type="button" value="Next" onClick="Next()"/>
		</center>
	</div>
  </div>
  <div class="tab-pane" id="avalanchetb">
</div>

		</div><!--end data table -->
	</div>
</div>

<hr/>
<div>
	<center>
		<h1>Clustering</h1>
	</center>
<div style="margin-left:2%;">
	<p>
	Cluster Points:
	</p>
	<p>
	<form name="statsform" id="statsform">
		X:
		<select name="o1" id="o1">
			<option value="time">Time</option>
			<option value="elevation" selected>Elevation</option>
			<option value="precipitation">Precipitation</option>
			<option value="month">Month</option>
			<option value="angle">Angle</option>
		</select>
		&nbsp;
		Y:
		<select name="o2" id="o2">
			<option value="elevation">Elevation</option>
			<option value="width">Width</option>
			<option value="precipitation">Precipitation</option>
			<option value="pathsize" selected>Path Size</option>
		</select>
	</form>
	<input type="button" onClick="getStats()" value="Get Stats"/>
	</p>
	<p>
		<div id="statstable" name="statstable">
		</div>
	</p>
</div>
</div>
<div id="contact">
	<h2 class="text-center">Contact us</h2>
	<div class="text-center">
	<address>
		<strong>Email us</strong>
		<a href="mailto:#">aevans30@msudenver.edu</a>
	</address>
</div>

</div><!--end container-->
		<!-- Google Map canvas src-->
		<!-- <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>-->
		  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=visualization"></script>
		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="http://www.powdernow.com/test/js/bootstrap.min.js"></script>
    <script src="http://www.powdernow.com/test/js/googleb.js"></script>
    <script src="http://www.powdernow.com/test/js/heat.js"></script>
 
    <!--<script src="js/googleip.js"></script>  -->
    <script>google.maps.event.addDomListener(window, 'load', initialize);</script>
	</body>
</html>
