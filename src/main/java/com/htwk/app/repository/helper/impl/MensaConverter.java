package com.htwk.app.repository.helper.impl;

import java.io.IOException;
import java.io.StringReader;

import javax.naming.directory.InvalidAttributesException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.mensa.Meal;
import com.htwk.app.repository.helper.XMLConverter;

public class MensaConverter extends XMLConverter {

	public Day<Meal> getObject(String content) throws InvalidAttributesException, XmlPullParserException, IOException {
		if (content == null || content.isEmpty()) {
			throw new InvalidAttributesException();
		}
		Day<Meal> day = new Day<Meal>();

		factory = XmlPullParserFactory.newInstance();
		parser = factory.newPullParser();
		parser.setInput(new StringReader(content));

		int eventType = parser.getEventType();
		int i = 0;
		Meal meal = new Meal();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {

			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:

				String tagName = parser.getName();

				if (tagName.equalsIgnoreCase("name")) {
					String temp = parser.nextText();
					if (temp != null) {
						meal.setTitle(temp);
						logger.debug(temp);
					}
				} else if (tagName.equalsIgnoreCase("price")) {
					Double temp = new Double(parser.nextText());
					if (temp != null) {
						meal.getPrice().put(meal.getPrice().size(), temp);
						logger.debug("" + temp);
					}
				} else if (tagName.equalsIgnoreCase("tagging")) {
					String temp = parser.nextText();
					if (temp != null) {
						meal.getAddititves().put(0, temp);
						logger.debug("" + temp);
					}
				} else if (tagName.equalsIgnoreCase("name1")) {
					String temp = parser.nextText();
					if (temp != null) {
						meal.getIngredients().add(temp);
						logger.debug(temp);
					}
				}
			case XmlPullParser.END_TAG:
				String endTag = parser.getName();
				if (endTag.equalsIgnoreCase("group")) {
					if ((i % 2) != 0) {
						day.getDayContent().add(meal);
						meal = new Meal();
					}
					i++;
				}
				break;
			}
			eventType = parser.next();
		}
		return day;
	}
}
