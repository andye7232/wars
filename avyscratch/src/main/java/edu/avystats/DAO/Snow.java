package edu.avystats.DAO;

public class Snow {
	private int avalancheID;
	private int locationID;
	private int year;
	private int month;
	private int day;
	private double waterequivalent;
	private int snowdepth;
	
	public Snow(){
		
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

	public void setLocationID(int location_id) {
		this.locationID = location_id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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

	public double getWaterequivalent() {
		return waterequivalent;
	}

	public void setWaterequivalent(double waterequivalent) {
		this.waterequivalent = waterequivalent;
	}

	public int getSnowdepth() {
		return snowdepth;
	}

	public void setSnowdepth(int snowdepth) {
		this.snowdepth = snowdepth;
	}
}
