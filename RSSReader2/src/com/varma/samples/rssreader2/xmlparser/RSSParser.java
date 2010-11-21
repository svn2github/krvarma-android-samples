package com.varma.samples.rssreader2.xmlparser;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.varma.samples.rssreader2.data.RSSChannel;
import com.varma.samples.rssreader2.data.RSSItem;

public class RSSParser extends DefaultHandler {
	private ArrayList<RSSItem> itemarray = null;
	private RSSItem rssitem = null; 
	private RSSChannel channel = null;
	private boolean isParsingChannel = false;
	private boolean isParsingItem = false;
	private String elementName = "";
	private StringBuilder builder = null;
	
	public RSSParser(ArrayList<RSSItem> itemarray) {
		super();
		
		this.itemarray = itemarray;
		this.channel = new RSSChannel();
		this.builder = new StringBuilder();
	}
	
	public RSSChannel getChannel(){
		return channel;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		
		builder.append(ch,start,length);
	}

	@Override
	public void endDocument() throws SAXException {
		
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		
		String value = builder.toString();
		
		if(elementName.equalsIgnoreCase("item")){
			isParsingItem = false;
		}
		else if(elementName.equalsIgnoreCase("title")){
			if(isParsingItem){
				rssitem.setTitle(value);
			}
			else if(isParsingChannel){
				channel.setTitle(value);
			}
		}
		else if(elementName.equalsIgnoreCase("description")){
			if(isParsingItem){
				rssitem.setDescription(value);
			}
			else if(isParsingChannel){
				channel.setDescription(value);
			}
		}
		else if(elementName.equalsIgnoreCase("link")){
			if(isParsingItem){
				rssitem.setLink(value);
			}
		}
		else if(elementName.equalsIgnoreCase("dc:creator")){
			if(isParsingItem){
				rssitem.setCreator(value);
			}
		}
		else if(elementName.equalsIgnoreCase("pubDate")){
			if(isParsingItem){
				rssitem.setLastupdated(value);
			}
		}
		else if(elementName.equalsIgnoreCase("lastBuildDate")){
			if(isParsingChannel){
				channel.setLastupdated(value);
			}
		}
	}

	@Override
	public void startDocument() throws SAXException {
		
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		elementName = qName;
		
		if(elementName.equalsIgnoreCase("channel")){
			isParsingChannel = true;
		}
		else if(elementName.equalsIgnoreCase("item")){
			isParsingItem = true;
			rssitem = new RSSItem();
			
			itemarray.add(rssitem);
		}
		else if(elementName.equalsIgnoreCase("atom:link") || elementName.equalsIgnoreCase("atom10:link")){
			channel.setLink(attributes.getValue("href"));
		}
		
		builder = new StringBuilder();
	}
}
