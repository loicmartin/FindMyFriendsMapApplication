package com.example.maps;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {
	public Account() {
		super();
	}
	public Account(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	@JsonProperty(value="Name")
	private String name;
	@JsonProperty(value="Email")			
	private String email;
	@JsonProperty(value="Password")
	private String password;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Account [getName()=" + getName() + ", getEmail()=" + getEmail()
				+ ", getPassword()=" + getPassword() + "]";
	}
	

	
}
