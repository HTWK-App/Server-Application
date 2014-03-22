package com.htwk.app.model.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7228726157987604599L;
	private String id;
	private String title;
	private String courseNumber;
	private String description;
	private String detailedLink;
	private String pictureLink;

	private String time;
	private String cycle;
	private String gender;
	private String leader;
	private String location;
	private String latLng;
	private String competitor;

	private List<String> hints;

	/**
	 * @return the id
	 */
	public synchronized final String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public synchronized final void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public synchronized final String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public synchronized final void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public synchronized final String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public synchronized final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the time
	 */
	public synchronized final String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public synchronized final void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the cycle
	 */
	public synchronized final String getCycle() {
		return cycle;
	}

	/**
	 * @param cycle
	 *            the cycle to set
	 */
	public synchronized final void setCycle(String cycle) {
		this.cycle = cycle;
	}

	/**
	 * @return the gender
	 */
	public synchronized final String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public synchronized final void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the leader
	 */
	public synchronized final String getLeader() {
		return leader;
	}

	/**
	 * @param leader
	 *            the leader to set
	 */
	public synchronized final void setLeader(String leader) {
		this.leader = leader;
	}

	/**
	 * @return the location
	 */
	public synchronized final String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public synchronized final void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the latLng
	 */
	public synchronized final String getLatLng() {
		return latLng;
	}

	/**
	 * @param latLng
	 *            the latLng to set
	 */
	public synchronized final void setLatLng(String latLng) {
		this.latLng = latLng;
	}

	/**
	 * @return the competitor
	 */
	public synchronized final String getCompetitor() {
		return competitor;
	}

	/**
	 * @param competitor
	 *            the competitor to set
	 */
	public synchronized final void setCompetitor(String competitor) {
		this.competitor = competitor;
	}

	/**
	 * @return the hints
	 */
	public synchronized final List<String> getHints() {
		if (hints == null) {
			hints = new ArrayList<String>();
		}
		return hints;
	}

	/**
	 * @param hints
	 *            the hints to set
	 */
	public synchronized final void setHints(List<String> hints) {
		this.hints = hints;
	}

	/**
	 * @return the pictureLink
	 */
	public synchronized final String getPictureLink() {
		return pictureLink;
	}

	/**
	 * @param pictureLink the pictureLink to set
	 */
	public synchronized final void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}

	/**
	 * @return the detailedLink
	 */
	public synchronized final String getDetailedLink() {
		return detailedLink;
	}

	/**
	 * @param detailedLink the detailedLink to set
	 */
	public synchronized final void setDetailedLink(String detailedLink) {
		this.detailedLink = detailedLink;
	}

	/**
	 * @return the courseNumber
	 */
	public synchronized final String getCourseNumber() {
		return courseNumber;
	}

	/**
	 * @param courseNumber the courseNumber to set
	 */
	public synchronized final void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

}
