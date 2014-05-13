package edu.avystats.DAO;

public class Storm{
	
	private int avalancheID;
	private int locationID;
	private int month;
	private int day;
	private int year;
	private double precipitation;
	private boolean rain;
	private boolean snow;
	private boolean thunder;
	private boolean fog;
	
	public Storm(){
		
	}

	public int getAvalancheID() {
		return avalancheID;
	}

	public void setAvalancheID(int avalanche_id) {
		this.avalancheID = avalanche_id;
	}
	
	

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}

	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}

	public boolean isRain() {
		return rain;
	}

	public void setRain(boolean rain) {
		this.rain = rain;
	}

	public boolean isSnow() {
		return snow;
	}

	public void setSnow(boolean snow) {
		this.snow = snow;
	}

	public boolean isThunder() {
		return thunder;
	}

	public void setThunder(boolean thunder) {
		this.thunder = thunder;
	}

	public boolean isFog() {
		return fog;
	}

	public void setFog(boolean fog) {
		this.fog = fog;
	}
	
	

}
