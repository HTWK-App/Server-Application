package com.htwk.app.model.weather;

import java.io.Serializable;

public class WeatherLocation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3169147439365512308L;
	private String id;
	private String name;
	private String country;
	/**
	 * @return the id
	 */
	public synchronized final String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public synchronized final void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public synchronized final String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public synchronized final void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the country
	 */
	public synchronized final String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public synchronized final void setCountry(String country) {
		this.country = country;
	}
}
