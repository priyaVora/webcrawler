package vora.priya.webcrawler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyRegexUtility implements RegexUtility {

	public int countContains(String needle, String haystack) {
		// needle is sentence
		// haystack is the term you wanna count for
		int count = 0;

		Pattern pattern = Pattern.compile(haystack);
		Matcher match = pattern.matcher(needle);

		while (match.find()) {
			count++;
		}

		return count;
	}

	public String getHTMLTagContents(String html, String tagName) {
		String rawRegex = "<" + tagName + ">(.*)<\\/" + tagName + ">";
		Pattern p = Pattern.compile(rawRegex);
		Matcher m = p.matcher(html);

		if (m.find()) {
			System.out.println("Group Count: " + m.groupCount());
			for (int i = 0; i <= m.groupCount(); i++) {
				System.out.println("Group " + i + ": " + m.group(i));
			}
		}

		return m.group(1);
	}

	public String[] getHTMLTagsContents(String html, String tagName) {
		String rawRegex = "<" + tagName + ">(.*?)<\\/" + tagName + ">";
		Pattern p = Pattern.compile(rawRegex);
		Matcher m = p.matcher(html);

		int count = 0;
		ArrayList<String> ids = new ArrayList<String>();

		while (m.find()) {
			String match = m.group(1);
			ids.add(match);
			count++;
		}
		String[] regexArray = new String[ids.size()];
		for (int i = 0; i < count; i++) {
			regexArray[i] = ids.get(i);
			System.out.println(regexArray[i]);
		}
		return regexArray;
	}

	public String[] getHTMLLinkURL(String html) {
		String link = "<a>(.*?)<\\/a>";
		Pattern p = Pattern.compile(link);
		Matcher m = p.matcher(html);

		int count = 0;
		ArrayList<String> linkList = new ArrayList<String>();

		while (m.find()) {
			String match = m.group(1);
			linkList.add(match);
			count++;
		}
		String[] linkArray = new String[linkList.size()];
		for (int i = 0; i < count; i++) {
			linkArray[i] = linkList.get(i);
			System.out.println(linkArray[i]);
		}
		return linkArray;
	}

	public String[] getBetweenBodyTag(String html) {

		String tag_value = null;
		String regex = "<body[^>]*>(.*?)(?:<.+>.*<.+>)(.*?)<\\/body>";
		Pattern pattern = Pattern.compile(regex, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);
		ArrayList<String> TagList = new ArrayList<String>();
		int count = 0;

		while (matcher.find()) {
			System.out.println("sdfsdfsdfs " + count);
			tag_value = matcher.group(count); // taking only group 1
			count++;
		}
		System.out.println("Body Tag: " + tag_value); // printing only group 1

		// String regexForBetweenBody =
		// "(.*?)(?:<.*[a-zA-Z]+>.*<\\/[a-zA-Z]+>)";
		// Pattern pTag = Pattern.compile(regexForBetweenBody);
		// Matcher mTag = pTag.matcher(tag_value);
		//
		// int bodyCount = 0;
		//
		// if (mTag.find()) {
		// String match = mTag.group(1);
		// TagList.add(match);
		//
		// while (mTag.find()) {
		// match = mTag.group(1);
		// TagList.add(match);
		//
		// }
		String[] Array = new String[TagList.size()];
		System.out.println("Text Outside of the Body Tag");
		for (int i = 0; i < TagList.size(); i++) {
			Array[i] = TagList.get(i);
			System.out.println((i + 1) + " " + Array[i]);
		}
		System.out.println("Array is being returned...");
		return Array;
		// String regexLastBit = "(?:<[a-zA-Z]+>.*<\\/[a-zA-Z]+>)(.*)";
		// Pattern plastTag = Pattern.compile(regexLastBit);
		// Matcher mlastTag = plastTag.matcher(tag_value);
		//
		// if (mlastTag.find()) {
		// String match1 = mlastTag.group(1);
		//
		// TagList.add(match1);
		// }
	}

	public String[] getTheLinks(String html) {
		// (href=".*?")
		String regexGrabsHref = "<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1";
		Pattern pattern = Pattern.compile(regexGrabsHref, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);

		ArrayList<String> hrefArray = new ArrayList<>();

		while (matcher.find()) {
			String match = matcher.group(2);
			hrefArray.add(match);
		}

		// for (int i = 0; i < hrefArray.size(); i++) {
		// System.out.println((i + 1) + " " + hrefArray.get(i));
		// }
		String[] href = new String[hrefArray.size()];

		for (int i = 0; i < hrefArray.size(); i++) {
			href[i] = hrefArray.get(i);
		}
		return href;
	}

	
	
	public String getDomain(String link) { 
		String domain = null;
		String regex = "http(?:s)?:\\/\\/([^\\/]+)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(link);
		
		if(!m.find() || m.groupCount() < 1) { 
			throw new RuntimeException("Couldn't determine domain: " + link);
		}
		domain = m.group(1);
		
		
		String regexRemoveWWW = "www.";
		if(domain.contains(regexRemoveWWW)) { 
			domain = domain.replaceAll(regexRemoveWWW, "");
		}
		return domain;
	}
	
	
	
	public ArrayList<String> getAllText(String html) { // make an if statement if none
		// existence do not do
		// anything....
		String[] wordArray = null;
		// remove any comments
		String removeComments = "(<\\!-?-?[\\s]?.*?-?-?>)";// "<\\!--[\\s]?(.*?)-->";
		Pattern removePattern = Pattern.compile(removeComments, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher removeMatcher = removePattern.matcher(html);

		int pCount = 0;
		String updatedPattern = null;
		while (removeMatcher.find()) {
			updatedPattern = removeMatcher.group(1);
			pCount++;
		}

		// You actually have to remove the comments from the string.
		//System.out.println("Before Pattern Removed" + html);
		for (int i = 0; i < pCount; i++) {
			html = html.replaceAll(removeComments, " ");

		}

		// remove javascript
		String removeJavaScript = "(<script[\\s]?.*?>(?:.*?)<\\/script>)"; // make
		// a
		// regex
		// that
		// grabs
		// the
		// javascript
		// then
		// replace
		// that
		// within
		// nothing
		// after

		Pattern removeJavaScriptPattern = Pattern.compile(removeJavaScript,
				Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher removeJavaScriptMatcher = removeJavaScriptPattern.matcher(html); // using
		// the
		// updated
		// version...

		int jCount = 0;
		String updatedJavaScript = null;

		//System.out.println("\nBefore JavaScript Updated: " + html); // put the
		// string
		// befpre
		// changed

		while (removeJavaScriptMatcher.find()) {
			updatedJavaScript = removeJavaScriptMatcher.group(1);
			jCount++;
		}

		for (int i = 0; i < jCount; i++) {
			html = html.replaceAll(removeJavaScript, "");
		}

		//System.out.println("\nJavaScript New : " + html);

		String link = "\\s*(<(?!\\/?(?=>|\\s.*>))\\/?.*?>)";
		Pattern p = Pattern.compile(link, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html); // grab the updated version instead

		int count = 0;
		while (m.find()) {
			String match = m.group(1);
			//System.out.println(match);
			count++;
		}

		//System.out.println(count);

		for (int i = 0; i < count; i++) {
			html = html.replaceAll(link, " ");
		}

		//System.out.println("Removed Tags : " + html);

		String regexDash = "([a-zA-Z]+)";
		Pattern pDash = Pattern.compile(regexDash, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher mDash = pDash.matcher(html);

		ArrayList<String> wordsList = new ArrayList<>();

		int counter = 0;
		while (mDash.find()) {
			String match1 = mDash.group(1);
			match1 = match1.toLowerCase();
			wordsList.add(match1);
			counter++;
		}
		return wordsList;
	}

	public ArrayList<String> getexternalLink(String domainnName, String html) {

		String regex = "(?:<a[\\s]?href=\")(http:\\/\\/(?:[a-zA-Z]+\\.[a-zA-Z_]+)+(?:\\.[a-zA-Z_]+)?(?:\\/?[a-zA-Z_]+\\/?\\.?)+)(?:\")";
		Pattern pattern = Pattern.compile(regex, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);

		ArrayList<String> ex = new ArrayList<>();
		ArrayList<String> onlyExternals = new ArrayList<>();
		while (matcher.find()) {
			String match = matcher.group(1);
			ex.add(match);
		}

		// System.out.println("External : ");
		// for (int i = 0; i < ex.size(); i++) {
		// System.out.println(ex.get(i));
		// }
		

		for (String url : ex) {
			if (!url.contains(domainnName)) {
				
				if(url.equals("http://my.neumont.edu/")) { 
					
				} else { 					
					onlyExternals.add(url);
				}
			}
		}

//		System.out.println("\nAll Externals");
//		for (String string : onlyExternals) {
//			System.out.println(string);
//		}

		return onlyExternals;
	}

	public ArrayList<String> getInternalLinks(String[] relativeLink, String[] absoluteLinks) {
		ArrayList<String> InternalCheckList = new ArrayList<>();

		for (int i = 0; i < relativeLink.length; i++) {
			InternalCheckList.add(relativeLink[i]);
		}

		for (int i = 0; i < absoluteLinks.length; i++) {
			InternalCheckList.add(absoluteLinks[i]);
		}
		//System.out.println("//----------------------------------------------------------------");
		//System.out.println("Internal Links : \n");

		ArrayList<String> checkList = new ArrayList<>();
		for (int i = 0; i < InternalCheckList.size(); i++) {

			if (checkList.contains(InternalCheckList.get(i))) {

			} else {
				checkList.add(InternalCheckList.get(i));
			}

		}

//		String[] checkArray = new String[checkList.size()];
//		for (int i = 0; i < checkArray.length; i++) {
//			checkArray[i] = checkList.get(i);
//		}
//		for (int i = 0; i < checkList.size(); i++) {
//			System.out.println((i + 1) + "  " + checkList.get(i));
//		}
		return checkList;
	}

	
	public String[] getTheAbsoluteLink(String html, String domainName) {
		String regexGrabTheAbsoluteLinks = "(?<=href=\")(http|https):\\/\\/.*?(?=\")";// "(?:<a[\\s]?href=\")(http:\\/\\/"
																						// +
																						// url
																						// +
																						// "(?:[\\/a-zA-Z0-9_\\-\\.]+)+)";
		Pattern pattern = Pattern.compile(regexGrabTheAbsoluteLinks, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);

		ArrayList<String> absArray = new ArrayList<>();
		while (matcher.find()) {
			String match = matcher.group(0);
			absArray.add(match);
		}

		String[] absArray2 = new String[absArray.size()];
		for (int i = 0; i < absArray2.length; i++) {
			absArray2[i] = absArray.get(i);
		}

		ArrayList<String> checkList = new ArrayList<>();
		for (int i = 0; i < absArray.size(); i++) {

			if (checkList.contains(absArray.get(i))) {

			} else {
				checkList.add(absArray.get(i));
			}
		}

		int sizeOfTheCheckArray = checkList.size();
		ArrayList<String> abs = new ArrayList<>();

		String[] checkArray = new String[sizeOfTheCheckArray];
		for (int i = 0; i < checkArray.length; i++) {
			checkArray[i] = checkList.get(i);
		}
//		System.out.println("--------------------------------------------------------------");
//		System.out.println("Absolute Link: ");

		for (String theUrl : checkArray) {
			if (theUrl.contains(domainName)) {
				abs.add(theUrl);
			}
		}

//		System.out.println("\nAll Absolutes");
//		for (String string : abs) {
//			System.out.println(string);
//		}
		int intAbs = abs.size();
		String[] newA = new String[intAbs];
		for (int i = 0; i < abs.size(); i++) {
			newA[i] = abs.get(i);
		}
		return newA;
	}

	public String[] getTheRelativeLinks(String html, String url) {
		String regexGrabsRelativeLinks = "(?<=href=\")((?!#)\\/?(?:\\/?[a-zA-Z0-9-])+\\.html)(?=\")";
		Pattern pattern = Pattern.compile(regexGrabsRelativeLinks, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);

		ArrayList<String> relArray = new ArrayList<>();

		while (matcher.find()) {
			String match = matcher.group(1);
			relArray.add(match);
		}
		int size = relArray.size();
		String[] reArray2 = new String[size];

		

		for (int i = 0; i < relArray.size(); i++) {
			reArray2[i] = url + "/" + relArray.get(i);
		}

		for (int i = 0; i < reArray2.length; i++) {
			reArray2[i] = reArray2[i].replaceAll("//", "/");
		}

		for (int i = 0; i < reArray2.length; i++) {
			reArray2[i] = reArray2[i].replaceAll("https:/", "https://");
		
			
		}

		ArrayList<String> checkList = new ArrayList<>();

		for (int i = 0; i < reArray2.length; i++) {

			if (checkList.contains(reArray2[i])) {

			} else {
				checkList.add(reArray2[i]);
			}
		}

		int sizeOfCheckarray = checkList.size();

		String[] checkArray = new String[sizeOfCheckarray];
		for (int i = 0; i < checkArray.length; i++) {
			checkArray[i] = checkList.get(i);
		}
//		System.out.println("/--------------------------------------");
//		System.out.println("Relative Links: ");
//		for (int i = 0; i < checkArray.length; i++) {
//			System.out.println((i + 1) + "  " + checkArray[i]);
//		}

		if (checkList.isEmpty()) {
			System.out.println("There are no relative links...!");
		}
		return checkArray;
	}

	public String[] getBetweenAltAttribute(String html) {
		String link = "(?:<[a-zA-Z\\s]+)(?:alt=')(.*)(?:'>)";
		Pattern p = Pattern.compile(link);
		Matcher m = p.matcher(html);

		int count = 0;
		ArrayList<String> linkList = new ArrayList<String>();

		while (m.find()) {
			String match = m.group(1);
			linkList.add(match);
			count++;
		}

		if (linkList.size() == 0) {
			System.out.println("There is no text within an Alt Attribute Tag!");
		}
		String[] AltAttributeArray = new String[linkList.size()];
		for (int i = 0; i < count; i++) {
			AltAttributeArray[i] = linkList.get(i);
			System.out.println(AltAttributeArray[i]);
		}
		return AltAttributeArray;
	}

}
