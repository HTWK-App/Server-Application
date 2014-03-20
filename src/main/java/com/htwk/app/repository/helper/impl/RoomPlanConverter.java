package com.htwk.app.repository.helper.impl;

import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.common.collect.ArrayListMultimap;
import com.htwk.app.model.room.Room;
import com.htwk.app.model.room.Room.RoomType;

public class RoomPlanConverter {

	public ArrayListMultimap<String, Room> getRoomList(String content) throws UnsupportedEncodingException {
		ArrayListMultimap<String, Room> rooms = ArrayListMultimap.create();
		
		Document doc = Jsoup.parse(content);
		for(Element gebaeude : doc.select("gebaeude")){
			String geb = new String(gebaeude.attr("name").getBytes("iso-8859-1"), "utf-8");
			
			for(Element types : gebaeude.select("raumtyp")){
				for(Element roomEntry : types.select("raum")){
					Room room = new Room();
					room.setId(new String(roomEntry.attr("id").getBytes("iso-8859-1"), "utf-8"));
					String name = new String(roomEntry.attr("name").getBytes("iso-8859-1"), "utf-8");
					room.setName(name.substring(0,name.indexOf("(")).trim());
					String type = new String(types.attr("name").getBytes("iso-8859-1"), "utf-8");
					room.setType(RoomType.valueOf(type));
					int size = Integer.parseInt(name.substring(name.indexOf("(")+1, name.indexOf(" ", name.indexOf("("))));
					room.setSize(size);
					rooms.put(geb,room);
				}
			}
		}
		return rooms;
	}
}
