package com.varma.samples.rssreader2.data;

public class RSSItem {
	private int id = 0;
	private long channelid = 0;
	private String title = "";
	private String link = "";
	private String description = "";
	private String lastupdated = "";
	private String pubdate = "";
	private String creater = "";
	private int isRead = 0;

	public RSSItem() {
		setId(0);
		setChannelid(0);
		setTitle("");
		setLink("");
		setDescription("");
		setLastupdated("");
		setPubdate("");
		setCreator("");
		setRead(0);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setChannelid(long channelid) {
		this.channelid = channelid;
	}

	public long getChannelid() {
		return channelid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}

	public String getLastupdated() {
		return lastupdated;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setCreator(String creator) {
		this.creater = creator;
	}

	public String getCreater() {
		return creater;
	}

	public void setRead(int isRead) {
		this.isRead = isRead;
	}

	public int isRead() {
		return isRead;
	}
}
