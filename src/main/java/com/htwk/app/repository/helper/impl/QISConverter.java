package com.htwk.app.repository.helper.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.htwk.app.model.qis.Modul;
import com.htwk.app.model.qis.Semester;

public class QISConverter {

	private static final Logger logger = LoggerFactory.getLogger(QISConverter.class);

	public List<Semester> getSemesterAsList(String content) {

		List<Semester> response = new ArrayList<Semester>();
		Document doc = Jsoup.parse(content);
		for (Element table : doc.select("table tbody")) {
			Semester semester = null;
			Modul modul = null;
			for (Element tr : table.select("tr")) {
				if (tr.hasClass("semester")) {
					response.add(semester);
					semester = new Semester();
					semester.setDescription(tr.text());
					semester.setId(semester.getDescription().substring(0, 1));
				}
				if (tr.hasClass("Mp") || tr.hasClass("Tl")) {
					Element[] td = tr.select("td").toArray(new Element[4]);
					modul = new Modul();
					modul.setId((td[0].text() == null) ? "" : td[0].text());

					String description = (td[1].text() == null) ? "" : td[1].text();
					modul.setTitle(description.split(" <")[0]);
					description = description.substring(description.indexOf("<") + 1, description.indexOf(">"));
					modul.setDescription(description);
					String[] desc = description.split(";");

					for (int i = 0; i < desc.length; i++) {
						if (desc[i].contains("am ")) {
							modul.setExamDate(desc[i]);
							if ((i + 1) < desc.length) {
								String prof = desc[i + 1];
								if (prof.contains("Versuch")) {
									prof = desc[i + 2];
								}
								modul.setProf(prof);
							}
						}
					}

					modul.setEcts((td[2].text() == null) ? "" : td[2].text());
					modul.setMark((td[3].text() == null) ? "" : td[3].text());

					semester.getModules().add(modul);
				}
				if (tr.hasClass("Pl")) {
					Element[] td = tr.select("td").toArray(new Element[4]);
					Modul submodul = new Modul();
					submodul.setId((td[0].text() == null) ? "" : td[0].text());
					String description = (td[1].text() == null) ? "" : td[1].text();
					submodul.setTitle(description.split(" <")[0]);
					description = description.substring(description.indexOf("<") + 1, description.indexOf(">"));
					submodul.setDescription(description);
					String[] desc = description.split(";");

					for (int i = 0; i < desc.length; i++) {
						if (desc[i].contains("am ")) {
							submodul.setExamDate(desc[i]);
							if ((i + 1) < desc.length) {
								String prof = desc[i + 1];
								if (prof.contains("Versuch")) {
									prof = desc[i + 2];
								}
								submodul.setProf(prof);
							}
						}
					}
					submodul.setEcts((td[2].text() == null) ? "" : td[2].text());
					submodul.setMark((td[3].text() == null) ? "" : td[3].text());

					if (modul == null) {
						semester.getModules().add(submodul);
					} else {
						modul.getSubmodul().add(submodul);
					}

				}
			}
			response.add(semester);
		}
		// remove all elements which are null
		response.removeAll(Collections.singleton(null));
		return response;
	}
}
