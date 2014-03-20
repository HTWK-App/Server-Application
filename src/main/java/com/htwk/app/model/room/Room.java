package com.htwk.app.model.room;

import java.io.Serializable;

public class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7799994301887525892L;
	private String id;
	private String name;
	private RoomType type;
	private int size;
	
	

	public static enum RoomType {
		Hörsaal, Seminarraum;

		public String getType() {
			return this.name();
		}
	}



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
	 * @return the type
	 */
	public synchronized final RoomType getType() {
		return type;
	}



	/**
	 * @param type the type to set
	 */
	public synchronized final void setType(RoomType type) {
		this.type = type;
	}



	/**
	 * @return the size
	 */
	public synchronized final int getSize() {
		return size;
	}



	/**
	 * @param size the size to set
	 */
	public synchronized final void setSize(int size) {
		this.size = size;
	}
}
