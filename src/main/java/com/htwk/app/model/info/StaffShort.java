package com.htwk.app.model.info;

import java.io.Serializable;

public class StaffShort implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9077641355471086140L;

	private String cuid;

	private String name;
	private String degree;

	private String faculty;

	public StaffShort(Staff staff) {
		this.cuid = staff.getCuid();
		this.name = staff.getName();
		this.degree = staff.getDegree();
		this.faculty = staff.getFaculty();
	}

	/**
	 * @return the cuid
	 */
	public synchronized final String getCuid() {
		return cuid;
	}

	/**
	 * @param cuid
	 *            the cuid to set
	 */
	public synchronized final void setCuid(String cuid) {
		this.cuid = cuid;
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
}
