package com.htwk.app.model.timetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5906497367194061943L;

	private String id;
	private String name;
	private List<String> semGroups;

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
	 * @return the semGroups
	 */
	public synchronized final List<String> getSemGroups() {
		if (semGroups == null) {
			semGroups = new ArrayList<String>();
		}
		return semGroups;
	}

	/**
	 * @param semGroups
	 *            the semGroups to set
	 */
	public synchronized final void setSemGroups(List<String> semGroups) {
		this.semGroups = semGroups;
	}

}
