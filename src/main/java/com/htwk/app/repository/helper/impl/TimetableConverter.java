package com.htwk.app.repository.helper.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;
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

	public List<Faculty> getSemGroup(String content) {

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
							// logger.debug(temp);
						}
						temp = new String(parser.getAttributeValue(1).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							faculty.setId(temp);
							// logger.debug(temp);
						}
					} else if (tagName.equalsIgnoreCase("studiengang")) {
						course = new Course();
						String temp = new String(parser.getAttributeValue(0).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							course.setName(temp);
							// logger.debug(temp);
						}

						temp = new String(parser.getAttributeValue(1).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							course.setId(temp);
							// logger.debug(temp);
						}
						faculty.getCourses().add(course);

					} else if (tagName.equalsIgnoreCase("semgrp")) {
						String temp = new String(parser.getAttributeValue(0).getBytes("iso-8859-1"), "utf-8");
						if (temp != null) {
							course.getSemGroups().add(temp);
							// logger.debug(temp);
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

	private Map<String, String> getProfs(String content) {
		Map<String, String> profs = new TreeMap<String, String>();

		Document doc = Jsoup.parse(content);
		for (Element tr : doc.select("kurztext")) {
			profs.put("" + tr.attr("id"), "" + tr.select("dozent").attr("name"));
		}
		return profs;
	}

	public Map<String, String> getCal(String content) throws XmlPullParserException, IOException {
		Map<String, String> cal = new TreeMap<String, String>();

		factory = XmlPullParserFactory.newInstance();
		parser = factory.newPullParser();
		parser.setInput(new StringReader(content));

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
				if (tagName.equalsIgnoreCase("period")) {
					String key = "semester";
					String value = new String(parser.getAttributeValue(1).getBytes("iso-8859-1"), "utf-8");
					cal.put(key, value);
				}
			case XmlPullParser.END_TAG:
				break;

			}
			eventType = parser.next();
		}
		return cal;

	}

	public List<Day<Subject>> getTimetable(String content, String profsContent) throws UnsupportedEncodingException {

		Map<String, String> profs = getProfs(profsContent);

		List<Day<Subject>> days = new ArrayList<Day<Subject>>();

		Document doc = Jsoup.parse(content);
		doc.select("table").first().remove();

		for (Element table : doc.select("table tbody")) {
			Day<Subject> day = new Day<Subject>();
			table.select("tr").first().remove();
			String[] weekdays = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };
			if (table.parent().previousElementSibling() != null
					&& ArrayUtils.contains(weekdays, table.parent().previousElementSibling().text())) {

				day.setId(table.parent().previousElementSibling().text());
				for (Element tr : table.select("tr")) {
					Element[] td = tr.select("td").toArray(new Element[8]);

					Subject subject = new Subject();

					subject.setKw((td[0] == null) ? new String[] {} : td[0].text().replaceAll("\\s+", "").split(","));
					subject.setBegin((td[1] == null) ? "" : td[1].text());
					subject.setEnd((td[2] == null) ? "" : td[2].text());
					subject.setLocation((td[3] == null) ? "" : td[3].text());
					subject.setDescription((td[4] == null) ? "" : td[4].text());
					subject.setType((td[5] == null) ? "" : td[5].text());
					subject.setDocent((td[6] == null) ? "" : td[6].text());
					String docentDetailed= (subject.getDocent()==null)?"":profs.get(subject.getDocent()); 
					docentDetailed = (docentDetailed==null)?"":new String(docentDetailed.getBytes("iso-8859-1"), "utf-8");
					subject.setDocentDetailed(docentDetailed);
					subject.setNotes((td[7] == null) ? "" : td[7].text());

					String suid = (subject.getDescription().length() > 15) ? subject.getDescription().substring(0, 15)
							: "";

					subject.setSuid(getSuid(suid));

					day.getDayContent().add(subject);

				}
				days.add(day);
			}
		}
		return days;
	}

	private String getSuid(String suid) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");

			md.update(suid.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("md5 algorithm not found:" + e);
		}
		return null;
	}
}
