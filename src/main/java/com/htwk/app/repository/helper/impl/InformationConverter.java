package com.htwk.app.repository.helper.impl;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.htwk.app.model.info.Staff;
import com.htwk.app.repository.helper.HTMLConverter;
import com.htwk.app.utils.MailUtils;
import com.htwk.app.utils.UrlUtils;

public class InformationConverter extends HTMLConverter {

	public List<Staff> getStaffList(String content) throws InvalidAttributesException {
		if (content == null || content.isEmpty()) {
			throw new InvalidAttributesException();
		}

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

	public Staff getStaffDetailed(Staff staff) throws IOException, InvalidAttributesException, ParseException {
		if (staff == null) {
			throw new InvalidAttributesException();
		}

		URL url = new URL(staff.getDetailLink());
		Document doc = Jsoup.parse(url, 1000);

		for (Element div : doc.select("div.phonelist_detail")) {
			staff.setFullName((div.select("h1") == null) ? "" : div.select("h1").first().text());
			staff.setDescription((div.select("div.info p") == null) ? "" : div.select("div.info p").first().text());
			staff.setTelefax((div.select("span.fax").first() == null) ? "" : div.select("span.fax")
					.first().text());
			staff.setPictureLink((div.select("div.info p") == null) ? "" : UrlUtils.getHtwkUrl(div
					.select("div.info img").first().attr("src")));
			staff.setPrivatePage((div.select("span.www a").first() == null) ? "" : UrlUtils.getHtwkUrl(div.select("span.www a")
					.first().attr("href")));
			
			String timestamp = (doc.select("p.last_update") == null)? "" : doc.select("p.last_update").text();
			timestamp = timestamp.substring(24,34);
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date date = dateFormat.parse(timestamp);
			logger.debug(""+timestamp+ ":"+date.toString());
			staff.setLastChange(new Timestamp(date.getTime()));
		}

		return staff;
	}
}
