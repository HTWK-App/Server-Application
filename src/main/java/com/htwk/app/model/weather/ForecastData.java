package com.htwk.app.model.weather;

import java.io.Serializable;
import java.util.List;

public class ForecastData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1162895203664943244L;
	private String cod;
	private Double message;

	private Object city;

	private long cnt;

	private List<WeatherDay> list;

	/**
	 * @return the cod
	 */
	public synchronized final String getCod() {
		return cod;
	}

	/**
	 * @param cod
	 *            the cod to set
	 */
	public synchronized final void setCod(String cod) {
		this.cod = cod;
	}

	/**
	 * @return the message
	 */
	public synchronized final Double getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public synchronized final void setMessage(Double message) {
		this.message = message;
	}

	/**
	 * @return the city
	 */
	public synchronized final Object getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public synchronized final void setCity(Object city) {
		this.city = city;
	}

	/**
	 * @return the cnt
	 */
	public synchronized final long getCnt() {
		return cnt;
	}

	/**
	 * @param cnt
	 *            the cnt to set
	 */
	public synchronized final void setCnt(long cnt) {
		this.cnt = cnt;
	}

	/**
	 * @return the list
	 */
	public synchronized final List<WeatherDay> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public synchronized final void setList(List<WeatherDay> list) {
		this.list = list;
	}
}
