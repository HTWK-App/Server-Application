package com.htwk.app.repository.helper.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.directory.InvalidAttributesException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.timetable.Course;
import com.htwk.app.model.timetable.Faculty;
import com.htwk.app.model.timetable.Subject;
import com.htwk.app.repository.helper.HTMLConverter;

public class TimetableConverter extends HTMLConverter {

	public List<Faculty> getSemGroup(String content) throws InvalidAttributesException {

		if (content == null || content.isEmpty()) {
			throw new InvalidAttributesException();
		}

		List<Faculty> faculties = new ArrayList<Faculty>();

		try {
			factory = XmlPullParserFactory.newInstance();
			parser = factory.newPullParser();
			parser.setInput(new StringReader(content));
			logger.debug("input-encoding {}", parser.getInputEncoding());

			int eventType = parser.getEventType();
			int i = 0;
			Faculty faculty = new Faculty();
			Course course = null;

			while (eventType != XmlPullParser.END_DOCUMENT) {

				switch (eventType) {

				case XmlPullParser.START_DOCUMENT:
					break;

				case XmlPullParser.START_TAG:
					String tagName = parser.getName();
					if (tagName.equalsIgnoreCase("fakultaet")) {
						String temp = new String(parser.getAttributeValue(0).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							faculty.setName(temp);
							logger.debug(temp);
						}
						temp = new String(parser.getAttributeValue(1).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							faculty.setId(temp);
							logger.debug(temp);
						}
					} else if (tagName.equalsIgnoreCase("studiengang")) {
						course = new Course();
						String temp = new String(parser.getAttributeValue(0).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							course.setName(temp);
							logger.debug(temp);
						}

						temp = new String(parser.getAttributeValue(1).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							course.setId(temp);
							logger.debug(temp);
						}
						faculty.getCourses().add(course);

					} else if (tagName.equalsIgnoreCase("semgrp")) {
						String temp = new String(parser.getAttributeValue(0).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							course.getSemGroups().add(temp);
							logger.debug(temp);
						}

					}
				case XmlPullParser.END_TAG:
					String endTag = parser.getName();
					if (endTag.equalsIgnoreCase("fakultaet")) {
						if ((i % 2) != 0) {
							faculties.add(faculty);
							faculty = new Faculty();
						}
						i++;
					}

					break;
				}

				eventType = parser.next();
			}
			return faculties;

		} catch (XmlPullParserException e) {
			logger.error("error while converting xml:", e);
		} catch (IOException e) {
			logger.error("error while converting xml:", e);
		}

		return null;
	}

	public Map<String, String> getCal(String content) throws InvalidAttributesException, XmlPullParserException, IOException {
		if (content == null || content.isEmpty()) {
			throw new InvalidAttributesException();
		}

		Map<String, String> cal = new TreeMap<String, String>();

		factory = XmlPullParserFactory.newInstance();
		parser = factory.newPullParser();
		parser.setInput(new StringReader(content));
		logger.debug("input-encoding {}", parser.getInputEncoding());

		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {

			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				String tagName = parser.getName();
				if (tagName.equalsIgnoreCase("woche")) {
					String key = new String(parser.getAttributeValue(1).getBytes("iso-8859-1"), "utf-8");
					String value = new String(parser.getAttributeValue(0).getBytes("iso-8859-1"), "utf-8");
					cal.put(key, value);

					if (value.equalsIgnoreCase("Alle Wochen")) {
						cal.put("all", key);
					}
				}
			case XmlPullParser.END_TAG:
				break;

			}
			eventType = parser.next();
		}
		return cal;

	}

	public List<Day<Subject>> getTimetable(String content) throws InvalidAttributesException {
		if (content == null || content.isEmpty()) {
			throw new InvalidAttributesException();
		}

		List<Day<Subject>> days = new ArrayList<Day<Subject>>();

		Document doc = Jsoup.parse(content);
		doc.select("table").first().remove();

		for (Element table : doc.select("table tbody")) {
			Day<Subject> day = new Day<Subject>();
			table.select("tr").first().remove();
			for (Element tr : table.select("tr")) {
				Element[] td = tr.select("td").toArray(new Element[8]);

				Subject subject = new Subject();

				subject.setBegin((td[1] == null) ? "" : td[1].text());
				subject.setEnd((td[2] == null) ? "" : td[2].text());
				subject.setLocation((td[3] == null) ? "" : td[3].text());
				subject.setDescription((td[4] == null) ? "" : td[4].text());
				subject.setType((td[5] == null) ? "" : td[5].text());
				subject.setDocent((td[6] == null) ? "" : td[6].text());
				subject.setNotes((td[7] == null) ? "" : td[7].text());
				day.getDayContent().add(subject);
			}
			// truncate the last tables which not needed
			if (days.size() < 5) {
				days.add(day);
			}
		}
		return days;
	}
}
