package com.htwk.app.model.weather;

import java.io.Serializable;

public class Temp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7650582338620522600L;
	private double day;
	private double min;
	private double max;
	private double night;
	private double eve;
	private double morn;
	/**
	 * @return the day
	 */
	public synchronized final double getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public synchronized final void setDay(double day) {
		this.day = day;
	}
	/**
	 * @return the min
	 */
	public synchronized final double getMin() {
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public synchronized final void setMin(double min) {
		this.min = min;
	}
	/**
	 * @return the max
	 */
	public synchronized final double getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public synchronized final void setMax(double max) {
		this.max = max;
	}
	/**
	 * @return the night
	 */
	public synchronized final double getNight() {
		return night;
	}
	/**
	 * @param night the night to set
	 */
	public synchronized final void setNight(double night) {
		this.night = night;
	}
	/**
	 * @return the eve
	 */
	public synchronized final double getEve() {
		return eve;
	}
	/**
	 * @param eve the eve to set
	 */
	public synchronized final void setEve(double eve) {
		this.eve = eve;
	}
	/**
	 * @return the morn
	 */
	public synchronized final double getMorn() {
		return morn;
	}
	/**
	 * @param morn the morn to set
	 */
	public synchronized final void setMorn(double morn) {
		this.morn = morn;
	}
}
