package com.htwk.app.model.qis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Modul implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7580435071758310706L;
	private String id;
	private String title;

	private String description;
	private String examDate;
	private String prof;

	private String ects;
	private String mark;

	private List<Modul> submodul;

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
	 * @return the ects
	 */
	public synchronized final String getEcts() {
		return ects;
	}

	/**
	 * @param ects
	 *            the ects to set
	 */
	public synchronized final void setEcts(String ects) {
		this.ects = ects;
	}

	/**
	 * @return the mark
	 */
	public synchronized final String getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            the mark to set
	 */
	public synchronized final void setMark(String mark) {
		this.mark = mark;
	}

	/**
	 * @return the submodul
	 */
	public synchronized final List<Modul> getSubmodul() {
		if (submodul == null) {
			submodul = new ArrayList<Modul>();
		}
		return submodul;
	}

	/**
	 * @param submodul
	 *            the submodul to set
	 */
	public synchronized final void setSubmodul(List<Modul> submodul) {
		this.submodul = submodul;
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
	 * @return the examDate
	 */
	public synchronized final String getExamDate() {
		return examDate;
	}

	/**
	 * @param examDate
	 *            the examDate to set
	 */
	public synchronized final void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	/**
	 * @return the prof
	 */
	public synchronized final String getProf() {
		return prof;
	}

	/**
	 * @param prof
	 *            the prof to set
	 */
	public synchronized final void setProf(String prof) {
		this.prof = prof;
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

}
