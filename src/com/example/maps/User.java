package com.example.maps;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
	@JsonProperty(value="ID")
	private String id;
	@JsonProperty(value="Name")
	private String name;
	@JsonProperty(value="Longitude")			
	private double longitude;
	@JsonProperty(value="Latitude")
	private double latitude;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "User [getLongitude()=" + getLongitude() + ", getLatitude()="
				+ getLatitude() + ", getName()=" + getName() + ", getId()="
				+ getId() + "]";
	}

	
}
