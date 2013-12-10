package com.htwk.app.model.info;


import java.io.Serializable;

public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6487566128544021768L;
	private String id;
	private String title;
	private String link;

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
	 * @return the link
	 */
	public synchronized final String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public synchronized final void setLink(String link) {
		this.link = link;
	}
}
