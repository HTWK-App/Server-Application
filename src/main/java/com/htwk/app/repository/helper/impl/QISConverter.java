package com.htwk.app.repository.helper.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
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
			List<Modul> submodules = new ArrayList<Modul>();
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

//					if(modul.getId().contains(submodul.getId().subSequence(0, submodul.getId().length()-1))){
						modul.getSubmodul().add(submodul);
//					}else{
//						submodules.add(submodul);
//						semester.getModules().add(submodul);
//					}
					

				}
			}
			/*
			for (int s = 0; s < submodules.size(); s++) {
//				boolean wasAdded = false;
				List<Modul> modules = semester.getModules();
				for (int m = 0; m < modules.size(); m++) {
					String sid = submodules.get(s).getId();
					if (modules.get(m).getId().contains(sid.subSequence(0, (sid.length() - 1)))) {
						modules.get(m).getSubmodul().add(submodules.get(s));
//						wasAdded = true;
//						continue;
					}
				}
//				if (!wasAdded) {
//					semester.getModules().add(submodules.get(s));
//				}
			}*/
			response.add(semester);
		}
		// remove all elements which are null
		response.removeAll(Collections.singleton(null));
		return response;
	}
}
