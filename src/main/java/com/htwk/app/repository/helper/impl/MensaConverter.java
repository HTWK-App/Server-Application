package com.htwk.app.repository.helper.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.mensa.Meal;
import com.htwk.app.repository.helper.XMLConverter;

public class MensaConverter extends XMLConverter {

	public Day<Meal> getObject(String content) {
		Day<Meal> day = new Day<Meal>();

		Document doc = Jsoup.parse(content);

		for (Element group : doc.select("group")) {
			Meal meal = new Meal();
			meal.setTitle(group.select("name").first().text());

			for (Element price : group.select("price")) {
				logger.debug("{},{}", price.attr("consumerID"), price.text());
				meal.getPrice().put(Integer.parseInt(price.attr("consumerID")), Double.parseDouble(price.text()));
			}
			for (Element tags : group.select("tagging")) {
				meal.getAddititves().put(Integer.parseInt(tags.attr("taggingID")), tags.text());
			}
			for (Element component : group.select("component")) {
				String componentName = component.select("name1").text();
				Elements taggings = component.select("tagging");
				for(Element tags : taggings){
					componentName += "["+tags.attr("taggingID")+"]"; 
				}
				meal.getIngredients().add(componentName);
				
			}
			day.getDayContent().add(meal);
		}
		return day;
	}

}