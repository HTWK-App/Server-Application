package com.htwk.app.model.weather;

import java.io.Serializable;

public class WeatherData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7334838771653879903L;
	private int id;
	private String main;
	private String description;
	private String descriptionDe;
	private String icon;
	private String iconData;
	/**
	 * @return the id
	 */
	public synchronized final int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public synchronized final void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the main
	 */
	public synchronized final String getMain() {
		return main;
	}
	/**
	 * @param main the main to set
	 */
	public synchronized final void setMain(String main) {
		this.main = main;
	}
	/**
	 * @return the description
	 */
	public synchronized final String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public synchronized final void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the descriptionDe
	 */
	public synchronized final String getDescriptionDe() {
		return descriptionDe;
	}
	/**
	 * @param descriptionDe the descriptionDe to set
	 */
	public synchronized final void setDescriptionDe(String descriptionDe) {
		this.descriptionDe = descriptionDe;
	}
	/**
	 * @return the iconSrc
	 */
	public synchronized final String getIcon() {
		return icon;
	}
	/**
	 * @param iconSrc the iconSrc to set
	 */
	public synchronized final void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the icon
	 */
	public synchronized final String getIconData() {
		return iconData;
	}
	/**
	 * @param icon the icon to set
	 */
	public synchronized final void setIconData(String iconData) {
		this.iconData = iconData;
	}
}
