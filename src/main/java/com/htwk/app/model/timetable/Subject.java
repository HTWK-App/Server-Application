package com.htwk.app.model.timetable;

import java.io.Serializable;

public class Subject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3204470690617560091L;

	private String begin;
	private String end;
	private String location;
	private String description;
	private String type;
	private String docent;
	private String notes;

	/**
	 * @return the begin
	 */
	public synchronized final String getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public synchronized final void setBegin(String begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public synchronized final String getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public synchronized final void setEnd(String end) {
		this.end = end;
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
	 * @return the type
	 */
	public synchronized final String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public synchronized final void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the docent
	 */
	public synchronized final String getDocent() {
		return docent;
	}

	/**
	 * @param docent
	 *            the docent to set
	 */
	public synchronized final void setDocent(String docent) {
		this.docent = docent;
	}

	/**
	 * @return the notes
	 */
	public synchronized final String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public synchronized final void setNotes(String notes) {
		this.notes = notes;
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
}
