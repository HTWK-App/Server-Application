package com.htwk.app.model.mensa;

import java.util.Collection;
import java.util.Collections;

import com.htwk.app.model.IDayContent;

public class Menu implements IDayContent<Meal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3990397802716504248L;

	private Collection<Meal> meals;

	/**
	 * @return the meals
	 */
	public synchronized final Collection<Meal> getMeals() {
		if (meals.isEmpty()) {
			meals = Collections.emptyList();
		}
		return meals;
	}

	/**
	 * @param meals
	 *            the meals to set
	 */
	public synchronized final void setMeals(Collection<Meal> meals) {
		this.meals = meals;
	}
}
