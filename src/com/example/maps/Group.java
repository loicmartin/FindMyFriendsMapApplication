package com.example.maps;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
public class Group implements Serializable {
	@JsonProperty("ID")
	private String id;
	@JsonProperty("Name")
	private String name;
	
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
		return "User [getId()=" + getId() + ", getName()=" + getName()
				+ "]";
	}
}
