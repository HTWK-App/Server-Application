package com.htwk.app.model.mail;

import java.io.Serializable;

public class MailAttachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4749000838791117637L;
	private String name;
	private int length;
	private String lengthFormated;
	private String type;
	
	/**
	 * @return the name
	 */
	public synchronized final String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public synchronized final void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the length
	 */
	public synchronized final int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public synchronized final void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the type
	 */
	public synchronized final String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public synchronized final void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the lengthFormated
	 */
	public synchronized final String getLengthFormated() {
		return lengthFormated;
	}
	/**
	 * @param lengthFormated the lengthFormated to set
	 */
	public synchronized final void setLengthFormated(String lengthFormated) {
		this.lengthFormated = lengthFormated;
	}
}
