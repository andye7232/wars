package edu.avystats.DAO;

public class Weather {
	
	private int avalancheID;
	private int month;
	private int day;
	private int year;
	private int locationID;
	private double temperature;
	private double sealevelpressure;
	private double visibility;
	private double maxtemperature;
	private double mintemperature;
	
	public Weather(){
		
	}
	
	public int getAvalancheID() {
		return avalancheID;
	}
	public void setAvalancheID(int avalanche_id) {
		this.avalancheID = avalanche_id;
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
	
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getSealevelpressure() {
		return sealevelpressure;
	}
	public void setSealevelpressure(double sealevelpressure) {
		this.sealevelpressure = sealevelpressure;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public double getMaxtemperature() {
		return maxtemperature;
	}
	public void setMaxtemperature(double maxtemperature) {
		this.maxtemperature = maxtemperature;
	}
	public double getMintemperature() {
		return mintemperature;
	}
	public void setMintemperature(double mintemperature) {
		this.mintemperature = mintemperature;
	}

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	
}
