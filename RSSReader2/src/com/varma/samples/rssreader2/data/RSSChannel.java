package com.varma.samples.rssreader2.data;

public class RSSChannel {
	private int id = -1;
	private String title = "";
	private String link = "";
	private String description = "";
	private String lastupdated = "";

	public RSSChannel() {
		setId(0);
		setTitle("");
		setLink("");
		setDescription("");
		setLastupdated("");
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
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
}
