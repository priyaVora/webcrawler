package vora.priya.webcrawler;

import java.io.Serializable;

public class UrlEntries implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int count;
	String url;

	public UrlEntries(int count, String url) {
		this.count = count;
		this.url = url;
	}
	
	public UrlEntries() {

	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
