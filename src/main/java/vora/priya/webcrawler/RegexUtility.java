package vora.priya.webcrawler;

import java.util.ArrayList;

public interface RegexUtility {


	int countContains(String needle, String haystack);
	
	/**
	 * Get the content of the first occurence of an html tag given it's name and the html string in which it occurs
	 * @param html the html string to be searched
	 * @param tagName the tagName for which the inner content should be returned
	 * @return
	 */
	String getHTMLTagContents(String html, String tagName);
	
	/**
	 * Get the content of all occurences of an html tag given it's name and the html string in which it occurs
	 * @param html the html string to be searched
	 * @param tagName the tagName for which the inner content should be returned
	 * @return
	 */
	String[] getHTMLTagsContents(String html, String tagName);
	
	/**
	 * For all occurrences of a link tag ("<a href=..") in the string html
	 * return the URL to which the link goes
	 * @param html the string to be searched
	 * @return an array of link destinations
	 */
	String[] getHTMLLinkURL(String html);
	
	String[] getBetweenBodyTag(String html);
	
	String[] getBetweenAltAttribute(String html);
	
	String[] getTheLinks(String html);
	
	ArrayList<String> getAllText(String html);
}
