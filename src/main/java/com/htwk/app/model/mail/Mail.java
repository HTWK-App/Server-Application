package com.htwk.app.model.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6333915831648373984L;

	private int id;
	private String from;
	private String subject;
	private List<String> toList;
	private List<String> ccList;
	private String sendDate;
	private String message;
	
	private List<String> flags;
	
	private List<MailAttachment> attachments;

	/**
	 * @return the id
	 */
	public synchronized final int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public synchronized final void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the from
	 */
	public synchronized final String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public synchronized final void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the subject
	 */
	public synchronized final String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public synchronized final void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the toList
	 */
	public synchronized final List<String> getToList() {
		if(toList == null){
			toList = new ArrayList<String>();
		}
		return toList;
	}

	/**
	 * @param toList
	 *            the toList to set
	 */
	public synchronized final void setToList(List<String> toList) {
		this.toList = toList;
	}

	/**
	 * @return the ccList
	 */
	public synchronized final List<String> getCcList() {
		if(ccList == null){
			ccList = new ArrayList<String>();
		}
		return ccList;
	}

	/**
	 * @param ccList
	 *            the ccList to set
	 */
	public synchronized final void setCcList(List<String> ccList) {
		this.ccList = ccList;
	}

	/**
	 * @return the sendDate
	 */
	public synchronized final String getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate
	 *            the sendDate to set
	 */
	public synchronized final void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the message
	 */
	public synchronized final String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public synchronized final void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the attachments
	 */
	public synchronized final List<MailAttachment> getAttachments() {
		if(attachments == null){
			attachments = new ArrayList<MailAttachment>();
		}
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public synchronized final void setAttachments(List<MailAttachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the flags
	 */
	public synchronized final List<String> getFlags() {
		if(flags == null){
			flags = new ArrayList<String>();
		}
		return flags;
	}

	/**
	 * @param flags the flags to set
	 */
	public synchronized final void setFlags(List<String> flags) {
		this.flags = flags;
	}
}
