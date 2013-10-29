package com.htwk.app.model.timetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Faculty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2600474865524434037L;

	private String id;
	private String name;

	private List<Course> courses = null;

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
	 * @return the StringList
	 */
	public synchronized final List<Course> getCourses() {
		if (courses == null) {
			courses = new ArrayList<Course>();
		}
		return courses;
	}

	/**
	 * @param StringList
	 *            the StringList to set
	 */
	public synchronized final void setCourses(List<Course> courses) {
		this.courses = courses;
	}
}
