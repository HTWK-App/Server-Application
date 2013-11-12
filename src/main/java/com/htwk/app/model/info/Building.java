package com.htwk.app.model.info;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Building implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4622701882696713576L;
	private String id;
	private String fullName;
	private String detailLink;
	private List<String> description;
	private String latLng;
	private String address;
	private String pictureLink;
	
	private Timestamp lastChange;

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
	 * @return the latLng
	 */
	public synchronized final String getLatLng() {
		return latLng;
	}

	/**
	 * @param latLng the latLng to set
	 */
	public synchronized final void setLatLng(String latLng) {
		this.latLng = latLng;
	}

	/**
	 * @return the address
	 */
	public synchronized final String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public synchronized final void setAddress(String address) {
		this.address = address;
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
	 * @return the description
	 */
	public synchronized final List<String> getDescription() {
		if(description == null){
			this.description = new ArrayList<String>();
		}
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public synchronized final void setDescription(List<String> description) {
		this.description = description;
	}

}
