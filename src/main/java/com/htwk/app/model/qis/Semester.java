package com.htwk.app.model.qis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Semester implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8087085005960268408L;
	private String id;
	private String description;
	private List<Modul> modules;
	
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
	/**
	 * @return the modules
	 */
	public synchronized final List<Modul> getModules() {
		if(modules == null){
			modules = new ArrayList<Modul>();
		}
		return modules;
	}
	/**
	 * @param modules the modules to set
	 */
	public synchronized final void setModules(List<Modul> modules) {
		this.modules = modules;
	}
}
