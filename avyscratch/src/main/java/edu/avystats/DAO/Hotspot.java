package edu.avystats.DAO;

public class Hotspot {
	
	private double longitude;
	private double latitude;
	private int frequency;
	private int locationID;
	
	public Hotspot(){
		
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int freq) {
		this.frequency = freq;
	}

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	
	
}
