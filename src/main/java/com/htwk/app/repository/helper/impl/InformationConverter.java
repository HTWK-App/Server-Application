package com.htwk.app.repository.helper.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.repository.helper.HTMLConverter;
import com.htwk.app.utils.MailUtils;
import com.htwk.app.utils.UrlUtils;

public class InformationConverter extends HTMLConverter {

	public List<Staff> getStaffList(String content) {

		List<Staff> staffMembers = new ArrayList<Staff>();

		Document doc = Jsoup.parse(content);
		for (Element table : doc.select("table")) {
			table.select("tr").first().remove();

			for (Element tr : table.select("tr")) {
				Element[] td = tr.select("td").toArray(new Element[6]);
				Staff staff = new Staff();
				String link = td[0].select("a").first().attr("href");
				staff.setDetailLink(UrlUtils.getHtwkUrl(link));
				staff.setCuid(link.substring(link.indexOf("cuid") + 5, link.length() - 1));

				staff.setName((td[0] == null) ? "" : td[0].text());
				staff.setDegree((td[1] == null) ? "" : td[1].text());
				staff.setFaculty((td[2] == null) ? "" : td[2].text());
				staff.setLocation((td[3] == null) ? "" : td[3].text());
				staff.setTelephone((td[4] == null) ? "" : td[4].text());

				staff.setFacultyLink(UrlUtils.getHtwkUrl(td[2].select("a").attr("href")));
				staff.setLocationLink(UrlUtils.getHtwkUrl(td[3].select("a").attr("href")));

				if (td[5] != null) {
					Element[] a = td[5].select("a").toArray(new Element[2]);
					staff.setVcardLink((a[0] == null) ? "" : UrlUtils.getHtwkUrl(a[0].attr("href")));
					String encMail = (a[1] == null) ? "" : a[1].attr("href");

					// truncate suffix and prefix from encripted mail address
					// and decrypt it
					if (encMail != null && !encMail.isEmpty()) {
						encMail = MailUtils.decryptMail(encMail).substring(40, encMail.length() - 3);
						staff.setEmail(encMail);
					}
				}

				staffMembers.add(staff);
			}
		}

		return staffMembers;
	}

	public Staff getStaffDetailed(Staff staff) throws IOException, ParseException {

		URL url = new URL(staff.getDetailLink());
		Document doc = Jsoup.parse(url, 5000);

		for (Element div : doc.select("div.phonelist_detail")) {
			staff.setFullName((div.select("h1") == null) ? "" : div.select("h1").first().text());
			staff.setDescription((div.select("div.info p") == null) ? "" : div.select("div.info p").first().text());
			staff.setTelefax((div.select("span.fax").first() == null) ? "" : div.select("span.fax").first().text());
			staff.setPictureLink((div.select("div.info p") == null) ? "" : UrlUtils.getHtwkUrl(div
					.select("div.info img").first().attr("src")));
			staff.setPrivatePage((div.select("span.www a").first() == null) ? "" : UrlUtils.getHtwkUrl(div
					.select("span.www a").first().attr("href")));

			String timestamp = (doc.select("p.last_update") == null) ? "" : doc.select("p.last_update").text();
			timestamp = timestamp.substring(24, 34);
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date date = dateFormat.parse(timestamp);
			// logger.debug("" + timestamp + ":" + date.toString());
			staff.setLastChange(new Timestamp(date.getTime()));
		}

		return staff;
	}

	public Building getBuildingInfo(String detailLink) throws IOException, ParseException {

		URL url = new URL(detailLink);
		logger.debug("fetch HTML-Doc for url:" + detailLink);
		Document doc = Jsoup.parse(url, 5000);

		for (Element main : doc.select("div#content")) {
			logger.debug("found main content in document");
			Building building = new Building();
			String fullName = main.select("h1.csc-firstHeader").text();
			building.setId(fullName.substring(fullName.indexOf("(") + 1, fullName.indexOf(")")));
			building.setFullName(fullName);

			building.setPictureLink((main.select("div.csc-default img").first() == null) ? "" : UrlUtils
					.getHtwkUrl(main.select("div.csc-default img").first().attr("src")));

			Elements p = main.select("div.csc-default").first().select("div.csc-textpic-text p");

			// separate Street and Postcode/City by ", "
			building.setAddress(Jsoup.parse(p.last().select("b").html().replace("<br />", ", ")).text());

			for (Element pDescription : p) {
				building.getDescription().add(pDescription.text());
			}
			building.getDescription().remove(building.getDescription().size() - 1);

			building.setDetailLink(detailLink);

			Element script = main.select("div.tx-lumogooglemaps-pi1 script").last();
			String latlng = script.html().substring(script.html().indexOf("new google.maps.LatLng("));
			latlng = latlng.substring(23, latlng.indexOf(")"));
			logger.debug("detected latlng :" + latlng);
			building.setLatLng(latlng);

			String timestamp = (main.select("p.last_update") == null) ? "" : doc.select("p.last_update").text();
			timestamp = timestamp.substring(24, 34);
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date date = dateFormat.parse(timestamp);
			logger.debug("found timestamp :" + timestamp);
			building.setLastChange(new Timestamp(date.getTime()));

			return building;
		}
		return null;
	}

	public List<Sport> getSportList(String content) throws UnsupportedEncodingException {

		List<Sport> sports = new ArrayList<Sport>();

		Document doc = Jsoup.parse(content);
		for (Element div : doc.select("div.event-info-box")) {
			Sport sport = new Sport();
			sport.setId(div.attr("id"));
			sport.setTitle((div.select("h4") == null) ? "" : div.select("h4").text());
			div.select("h4").remove();
			sport.setDetailedLink((div.select("a") == null) ? "" : "http://sport.htwk-leipzig.de"
					+ div.select("a").attr("href"));
			sport.setPictureLink((div.select("a img") == null) ? "" : div.select("a img ").attr("src"));
			div.removeClass("a");
			sport.setDescription(new String(div.text().replace("\u00a0", "").getBytes("iso-8859-1"), "utf-8"));
			sports.add(sport);
		}

		return sports;
	}

	public Sport getSportDetailed(Sport sport) throws IOException {

		URL url = new URL(sport.getDetailedLink());

		Document doc = Jsoup.parse(url, 5000);
		Element div = doc.select("dl.event-infos").first();
		Element[] dd = div.select("dd").toArray(new Element[10]);
		sport.setTime((dd[0] == null) ? "" : dd[0].text());
		sport.setCycle((dd[1] == null) ? "" : dd[1].text());
		sport.setGender((dd[2] == null) ? "" : dd[2].text());
		sport.setLeader((dd[3] == null) ? "" : dd[3].text());
		sport.setLocation((dd[4] == null) ? "" : dd[4].text().replace(" Karte", ""));
		
		sport.setLatLng(null);
		sport.setCompetitor((dd[5] == null) ? "" : dd[5].text());

		for (Element hint : doc.select("dl.event-infos").last().select("dd")) {
			sport.getHints().clear();
			sport.getHints().add((hint == null) ? "" : hint.text());
		}
		return sport;
	}
}
