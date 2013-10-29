package com.htwk.app.model.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Day<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7201249474959483973L;

	private List<T> dayContent = null;

	/**
	 * @return the dayContent
	 */
	public synchronized final List<T> getDayContent() {
		if(dayContent == null){
			dayContent = new ArrayList<T>();
		}
		return dayContent;
	}

	/**
	 * @param dayContent
	 *            the dayContent to set
	 */
	public synchronized final void setDayContent(List<T> dayContent) {
		this.dayContent = dayContent;
	}
}
