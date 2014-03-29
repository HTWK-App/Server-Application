package com.htwk.app.model.weather;

import java.io.Serializable;

public class WeatherDay implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6639731842618409838L;
	private long dt;
	private Temp temp;

	private double pressure;
	private long humidity;

	private WeatherData[] weather;
	private double speed;
	private long deg;
	private long clouds;
	/**
	 * @return the dt
	 */
	public synchronized final long getDt() {
		return dt;
	}
	/**
	 * @param dt the dt to set
	 */
	public synchronized final void setDt(long dt) {
		this.dt = dt;
	}
	/**
	 * @return the temp
	 */
	public synchronized final Temp getTemp() {
		return temp;
	}
	/**
	 * @param temp the temp to set
	 */
	public synchronized final void setTemp(Temp temp) {
		this.temp = temp;
	}
	/**
	 * @return the pressure
	 */
	public synchronized final double getPressure() {
		return pressure;
	}
	/**
	 * @param pressure the pressure to set
	 */
	public synchronized final void setPressure(double pressure) {
		this.pressure = pressure;
	}
	/**
	 * @return the humidity
	 */
	public synchronized final long getHumidity() {
		return humidity;
	}
	/**
	 * @param humidity the humidity to set
	 */
	public synchronized final void setHumidity(long humidity) {
		this.humidity = humidity;
	}
	/**
	 * @return the weather
	 */
	public synchronized final WeatherData[] getWeather() {
		return weather;
	}
	/**
	 * @param weather the weather to set
	 */
	public synchronized final void setWeather(WeatherData[] weather) {
		this.weather = weather;
	}
	/**
	 * @return the speed
	 */
	public synchronized final double getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public synchronized final void setSpeed(double speed) {
		this.speed = speed;
	}
	/**
	 * @return the deg
	 */
	public synchronized final long getDeg() {
		return deg;
	}
	/**
	 * @param deg the deg to set
	 */
	public synchronized final void setDeg(long deg) {
		this.deg = deg;
	}
	/**
	 * @return the clouds
	 */
	public synchronized final long getClouds() {
		return clouds;
	}
	/**
	 * @param clouds the clouds to set
	 */
	public synchronized final void setClouds(long clouds) {
		this.clouds = clouds;
	}
}
