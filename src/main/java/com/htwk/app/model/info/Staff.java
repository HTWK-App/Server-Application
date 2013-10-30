package com.htwk.app.model.info;

import java.io.Serializable;
import java.sql.Timestamp;

public class Staff implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2548396949095256996L;

	private String cuid;
	private String detailLink;
	
	private String name;
	private String degree;
	private String faculty;
	private String facultyLink;
	private String location;
	private String locationLink;
	private String telephone;
	private String email;
	private String vcardLink;

	private String pictureLink;
	private String fullName;
	private String telefax;
	private String description;
	private String privatePage;
	private Timestamp lastChange;

	/**
	 * @return the name
	 */
	public synchronized final String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public synchronized final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the degree
	 */
	public synchronized final String getDegree() {
		return degree;
	}

	/**
	 * @param degree
	 *            the degree to set
	 */
	public synchronized final void setDegree(String degree) {
		this.degree = degree;
	}

	/**
	 * @return the faculty
	 */
	public synchronized final String getFaculty() {
		return faculty;
	}

	/**
	 * @param faculty
	 *            the faculty to set
	 */
	public synchronized final void setFaculty(String faculty) {
		this.faculty = faculty;
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
	 * @return the telephone
	 */
	public synchronized final String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public synchronized final void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the email
	 */
	public synchronized final String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public synchronized final void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the vcardLink
	 */
	public synchronized final String getVcardLink() {
		return vcardLink;
	}

	/**
	 * @param vcardLink the vcardLink to set
	 */
	public synchronized final void setVcardLink(String vcardLink) {
		this.vcardLink = vcardLink;
	}

	/**
	 * @return the telefax
	 */
	public synchronized final String getTelefax() {
		return telefax;
	}

	/**
	 * @param telefax the telefax to set
	 */
	public synchronized final void setTelefax(String telefax) {
		this.telefax = telefax;
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
	 * @return the privatePage
	 */
	public synchronized final String getPrivatePage() {
		return privatePage;
	}

	/**
	 * @param privatePage the privatePage to set
	 */
	public synchronized final void setPrivatePage(String privatePage) {
		this.privatePage = privatePage;
	}

	/**
	 * @return the lastChange
	 */
	public synchronized final Timestamp getLastChange() {
		return lastChange;
	}

	/**
	 * @param lastChange the lastChange to set
	 */
	public synchronized final void setLastChange(Timestamp lastChange) {
		this.lastChange = lastChange;
	}

	/**
	 * @return the cuid
	 */
	public synchronized final String getCuid() {
		return cuid;
	}

	/**
	 * @param cuid the cuid to set
	 */
	public synchronized final void setCuid(String cuid) {
		this.cuid = cuid;
	}

	/**
	 * @return the detailLink
	 */
	public synchronized final String getDetailLink() {
		return detailLink;
	}

	/**
	 * @param detailLink the detailLink to set
	 */
	public synchronized final void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}

	/**
	 * @return the facultyLink
	 */
	public synchronized final String getFacultyLink() {
		return facultyLink;
	}

	/**
	 * @param facultyLink the facultyLink to set
	 */
	public synchronized final void setFacultyLink(String facultyLink) {
		this.facultyLink = facultyLink;
	}

	/**
	 * @return the locationLink
	 */
	public synchronized final String getLocationLink() {
		return locationLink;
	}

	/**
	 * @param locationLink the locationLink to set
	 */
	public synchronized final void setLocationLink(String locationLink) {
		this.locationLink = locationLink;
	}

	/**
	 * @return the fullName
	 */
	public synchronized final String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public synchronized final void setFullName(String fullName) {
		this.fullName = fullName;
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
}
