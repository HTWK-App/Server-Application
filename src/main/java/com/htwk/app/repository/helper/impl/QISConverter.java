package com.htwk.app.repository.helper.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.htwk.app.model.qis.Modul;
import com.htwk.app.model.qis.Semester;

public class QISConverter {

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
				if (tr.hasClass("Mp")) {
					Element[] td = tr.select("td").toArray(new Element[4]);
					modul = new Modul();
					modul.setId((td[0].text() == null) ? "" : td[0].text());
					String description = (td[1].text() == null) ? "" : td[1].text();
					modul.setTitle(description.split(" <")[0]);
					modul.setDescription(description.substring(description.indexOf("<")+1, description.indexOf(">")));
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
					submodul.setDescription(description.substring(description.indexOf("<")+1, description.indexOf(">")));
					submodul.setEcts((td[2].text() == null) ? "" : td[2].text());
					submodul.setMark((td[3].text() == null) ? "" : td[3].text());
					modul.getSubmodul().add(submodul);
				}
			}
			response.add(semester);
		}
		//remove all elements which are null
		response.removeAll(Collections.singleton(null));
		return response;
	}
}
