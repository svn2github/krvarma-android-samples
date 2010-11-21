package com.varma.samples.rssreader2.utility;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.varma.samples.rssreader2.data.RSSChannel;
import com.varma.samples.rssreader2.data.RSSItem;

public class Utility {
	public static void retrieveRSSFeed(String urlToRssFeed,ArrayList<RSSItem> list, RSSChannel channel, DefaultHandler handler)
    {
        try
        {
           URL url = new URL(urlToRssFeed);
           SAXParserFactory factory = SAXParserFactory.newInstance();
           SAXParser parser = factory.newSAXParser();
           XMLReader xmlreader = parser.getXMLReader();

           xmlreader.setContentHandler(handler);

           InputSource is = new InputSource(url.openStream());

           xmlreader.parse(is);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
