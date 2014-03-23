package com.htwk.app.repository.helper.impl;

import java.io.IOException;
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

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.timetable.Course;
import com.htwk.app.model.timetable.Faculty;
import com.htwk.app.model.timetable.Subject;
import com.htwk.app.repository.helper.HTMLConverter;

public class TimetableConverter extends HTMLConverter {

	public List<Faculty> getSemGroup(String content) throws UnsupportedEncodingException {

		List<Faculty> faculties = new ArrayList<Faculty>();

		Document doc = Jsoup.parse(content);
		for (Element faculty : doc.select("fakultaet")) {
			Faculty fac = new Faculty();
			fac.setId(new String(faculty.attr("id").getBytes("iso-8859-1"), "utf-8"));
			fac.setName(new String(faculty.attr("name").getBytes("iso-8859-1"), "utf-8"));
			for (Element course : faculty.select("studiengang")) {
				Course cou = new Course();
				cou.setId(new String(course.attr("id").getBytes("iso-8859-1"), "utf-8"));
				cou.setName(new String(course.attr("name").getBytes("iso-8859-1"), "utf-8"));
				for (Element semgroup : course.select("semgrp")) {
					cou.getSemGroups().add(semgroup.attr("name"));
				}
				fac.getCourses().add(cou);
			}
			faculties.add(fac);
		}
		return faculties;

	}

	private Map<String, String> getProfs(String content) {
		Map<String, String> profs = new TreeMap<String, String>();

		Document doc = Jsoup.parse(content);
		for (Element tr : doc.select("kurztext")) {
			profs.put("" + tr.attr("id"), "" + tr.select("dozent").attr("name"));
		}
		return profs;
	}

	public Map<String, String> getCal(String content) throws IOException {
		Map<String, String> cal = new TreeMap<String, String>();

		Document doc = Jsoup.parse(content);
		for (Element woche : doc.select("woche")) {
			String key = woche.attr("id");
			String value = woche.attr("name");
			cal.put(key, value);

			if (value.equalsIgnoreCase("Alle Wochen")) {
				cal.put("all", key);
			}
			if (value.contains("Aktuelle Woche")) {
				cal.put("current", key.replace("_", ""));
			}
		}
		cal.put("semester", doc.select("period").first().attr("id"));
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
					String docentDetailed = (subject.getDocent() == null) ? "" : profs.get(subject.getDocent());
					docentDetailed = (docentDetailed == null) ? "" : new String(docentDetailed.getBytes("iso-8859-1"),
							"utf-8");
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
