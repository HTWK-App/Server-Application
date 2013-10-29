package com.htwk.app.repository.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.thoughtworks.xstream.XStream;


public class XMLConverter implements IConverter {

	protected static final Logger logger = LoggerFactory.getLogger(XMLConverter.class);
	
	protected XmlPullParserFactory factory;
	protected XmlPullParser parser; 
}
