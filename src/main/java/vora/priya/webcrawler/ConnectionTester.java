package vora.priya.webcrawler;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement.GlobalScope;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

public class ConnectionTester {
	public static final int MAX_DEPTH = 2;
	MyRegexUtility r;
	ArrayList<String> globalStore = new ArrayList<>();
	
	static Map<String, ArrayList<UrlEntries>> stored = new HashMap<>();

	public void crawl(String url) throws IOException { // for clients who want to utilize the projects
		List<String> visitedLinks = new ArrayList<>();
		crawlAux(url, 0, visitedLinks);
	}

	private void crawlAux(String url, int currentDepth, List<String> visitedLinks) throws IOException {
		// go get the raw html as string
		String rawHtml = getRawHtmlFromSite(url);
		List<String> links = getLinksFromRawHtml(rawHtml, url);
		List<String> words = getWordsFromPage(rawHtml);

		WordData wd = new WordData("WordDataBase.txt");
		ArrayList<String> checkList = wd.checkList(words);
		Map<String, Integer> countMap = wd.setTheCount(words, checkList);

		for (int i = 0; i < checkList.size(); i++) {
			int theCount = wd.searchTheCount(countMap, checkList.get(i));
			Word w = wd.createAWord(checkList.get(i), url, theCount);
			UrlEntries entries = new UrlEntries(theCount, url);

			if (stored.containsKey(w.getWord())) {
				ArrayList<UrlEntries> tempValue = stored.get(w.getWord());

				for (int j = 0; j < tempValue.size(); j++) {

					if (tempValue.get(j).getUrl().equals(url)) {
						tempValue.get(j).setCount(theCount);
					} else {
						tempValue.add(entries);
					}
				}
				stored.put(w.getWord(), tempValue);
			} else {
				ArrayList<UrlEntries> newList = new ArrayList<>();
				newList.add(new UrlEntries(theCount, url));
				stored.put(w.getWord(), newList);
			}

			System.out.println("Word ToString : " + " " + w.toString());
	
			for (int k = 0; k < links.size(); k++) {
				if (globalStore.contains(links.get(k))) {
					links.remove(i);
				}
			}
			
			visitedLinks.add(url.toLowerCase());
			for (String link : links) {
				if (isExternalLInk(link, url) || currentDepth < MAX_DEPTH) {
					if (!visitedLinks.contains(link.toLowerCase())) {
						crawlAux(link, isExternalLInk(link, url) ? currentDepth + 1 : currentDepth, visitedLinks);
						System.out.println("Crawl to External: " + url);
					}
				} else {
					// here you should account for the internal link
					if(!visitedLinks.contains(link.toLowerCase())) { 					
						crawlAux(url, currentDepth, visitedLinks);
						System.out.println("Crawl to Internal: " + url);
					}
				}
			}
			}
		
		
	
		System.out.println("\nBody Tags for : " + url + "\n");
		 r.getBetweenBodyTag(url);
		 System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
		
		 System.out.println("\n*****Text Inside div Tag for : " + url +
		 "\n");
		 // r.getBetweenSpecificTags(html, "div");
		 System.out.println("\n*****Text Inside p Tag for : " + url +
		 "\n");
		 // r.getBetweenSpecificTags(html, "p");
		 System.out.println("\n*****Text Inside span Tag for : " + url +
		 "\n");
		 //r.getBetweenSpecificTags(html, "span");
		 System.out.println("\n*****Text Inside li Tag for : " + url +
		 "\n");
		 //r.getBetweenSpecificTags(html, "li");
		 System.out.println("\n*****Text Inside label Tag for : " + url +
		 "\n");
		 // r.getBetweenSpecificTags(html, "label");
		 System.out.println("\n*****Text Inside option Tag for : " + url +
		 "\n");
		 //r.getBetweenSpecificTags(html, "option");
		 System.out.println("\n*****Text Inside td Tag for : " + url +
		 "\n");
		 // r.getBetweenSpecificTags(html, "td");
		 System.out.println("\n*****Text Inside font Tag for : " + url +
		 "\n");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		indexWordsFromPage(words, url);
//		visitedLinks.add(url.toLowerCase());
//		for (String link : links) {
//			if (isExternalLInk(link, url) || currentDepth < MAX_DEPTH) {
//				if (!visitedLinks.contains(link.toLowerCase())) {
//					crawlAux(link, isExternalLInk(link, url) ? currentDepth + 1 : currentDepth, visitedLinks);
//					System.out.println("Crawl to External: " + url);
//				}
//			} else {
//				// here you should account for the internal link
//				if(!visitedLinks.contains(link.toLowerCase())) { 					
//					crawlAux(url, currentDepth, visitedLinks);
//					System.out.println("Crawl to Internal: " + url);
//				}
//			}
//		}
	}

	
	public static void main(String[] args) throws IOException {
		
		String[] arguement = new String[2]; 
		arguement[0] = "https://www.neumont.edu";
		arguement[1] = "https://www.york.ac.uk/teaching/cws/wws/webpage1.html";
		
		ConnectionTester tester = new ConnectionTester();

		WordData wd = new WordData("WebCrawlerDatabase.txt");
		String strURL = "https://www.neumont.edu";

		tester.crawl(strURL);
		
		
		
	}



	private boolean isExternalLInk(String link, String baseUrl) {
		// does it contain the neumont.edu? if o then external
		String baseDomain = r.getDomain(baseUrl);
		String checkDomain = r.getDomain(link);
		return baseDomain.replace("www.", "").equalsIgnoreCase(checkDomain.replace("www.", ""));
	}

	private String getRawHtmlFromSite(String strURL) throws IOException {
		Connection ce = new Connection();
		r = new MyRegexUtility();
		URL url = new URL(strURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		String html = "";
		html = ce.getInputFromConnection(con, html);
		return html;
	}
		
	
	private List<String> getWordsFromPage(String html) { 
		System.out.println("Entered getWordsFromPage Method");
		List<String> getWords = r.getAllText(html);
		System.out.println("Got all the text from page.");
		return getWords;
	}
	
	private void indexWordsFromPage(List<String> words, String url) throws IOException {
		System.out.println("Entered IndexWordsFromPage");
		WordData wd = new WordData("WordDataBase.txt");
		ArrayList<String> checkList = wd.checkList(words);
		Map<String, Integer> countMap = wd.setTheCount(words, checkList);

		for (int i = 0; i < checkList.size(); i++) {
			int theCount = wd.searchTheCount(countMap, checkList.get(i));
			Word w = wd.createAWord(checkList.get(i), url, theCount);
			UrlEntries entries = new UrlEntries(theCount, url);

			if (stored.containsKey(w.getWord())) {
				ArrayList<UrlEntries> tempValue = stored.get(w.getWord());

				for (int j = 0; j < tempValue.size(); j++) {

					if (tempValue.get(j).getUrl().equals(url)) {
						tempValue.get(j).setCount(theCount);
					} else {
						tempValue.add(entries);
					}
				}
				stored.put(w.getWord(), tempValue);
			} else {
				ArrayList<UrlEntries> newList = new ArrayList<>();
				newList.add(new UrlEntries(theCount, url));
				stored.put(w.getWord(), newList);
			}

			System.out.println("Word ToString : " + " " + w.toString());
		}
		
		
		 //r.getBetweenSpecificTags(html, "font");
		
		 System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
		 System.out.println("Text in an Alt Attribute: " + url + "\n");
		 r.getBetweenAltAttribute(url);
		 System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
		 System.out.println("Text Inside Title Tag for : " + url + "\n");
		 // r.getBetweenTitleTag(html);
	

		System.out.println("Finished Word To Stringa");
		
		System.out.println("\nStart of the FileOutput");
		try (FileOutputStream out = new FileOutputStream("Word_Data")) {
			try (ObjectOutputStream objOut = new ObjectOutputStream(out)) {
				objOut.writeObject(stored);
			}
		}
		System.out.println("Finished Wrting the stored to File.");
		System.out.println("About to leave the index method...");
		
	}

	private List<String> getLinksFromRawHtml(String html, String url) {
		String domainOfBase = r.getDomain(url);

		List<String> listOfLink = new ArrayList<>();
		String regex = "<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1";
		Pattern pattern = Pattern.compile(regex, Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);

		while (matcher.find()) {
			String match = matcher.group(2);
			listOfLink.add(match);
		}

		String regexCheckRelative = "(?<=href=\")((?!#)\\/?(?:\\/?[a-zA-Z0-9-])+\\.html)(?=\")";
		Pattern patternCheckRelative = Pattern.compile(regexCheckRelative,
				Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);

		//I could make an arrayList that stores the alterations
		//then do .replace all on the original within a forloop. 
		//Then just return whatever you had.
		List<String> alterList = new ArrayList<>();
		
		
	
			Matcher matcherCheckRelative = patternCheckRelative.matcher(html);
			while (matcherCheckRelative.find()) {
				String match = matcherCheckRelative.group(1);

				String finder = match;
				match = domainOfBase + "/" + match;
				match = match.replaceAll("//", "");
				
				if(!match.contains("https://")) { 
					match = "https://" + match;
				}
				
				for (int i = 0; i < listOfLink.size(); i++) {
					if(listOfLink.get(i).equals(finder)) { 
						listOfLink.remove(i);
						listOfLink.add(i, match);
					}
				}
				
				
				
				alterList.add(match);
			}
			//just do nothing to the link in that position...because it is within the correct format...
	
			for (int i = 0; i < listOfLink.size(); i++) {
				if(listOfLink.get(i).equalsIgnoreCase("/")) { 
					listOfLink.remove(i);
				} else if(listOfLink.get(i).equalsIgnoreCase("#")) { 
					listOfLink.remove(i);
				}
			}

		return listOfLink;
	}
	
}
















//public void crawl1(String strURL) throws IOException {
//globalStore.add(strURL);
//System.out.println("Crawl " + strURL);
//Connection ce = new Connection();
//r = new MyRegexUtility();
//// String domain = r.domainName(strURL);
//
//URL url = new URL(strURL);
//HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//String html = "";
//html = ce.getInputFromConnection(con, html);
//
//String[] wordArray = r.getAllText(html);
//
//WordData wd = new WordData("WordDataBase.txt");
//ArrayList<String> checkList = wd.checkList(wordArray);
//Map<String, Integer> countMap = wd.setTheCount(wordArray, checkList);
//
//for (int i = 0; i < checkList.size(); i++) {
//	int theCount = wd.searchTheCount(countMap, checkList.get(i));
//	Word w = wd.createAWord(checkList.get(i), strURL, theCount);
//	UrlEntries entries = new UrlEntries(theCount, strURL);
//
//	if (stored.containsKey(w.getWord())) {
//		ArrayList<UrlEntries> tempValue = stored.get(w.getWord());
//
//		for (int j = 0; j < tempValue.size(); j++) {
//
//			if (tempValue.get(j).getUrl().equals(strURL)) {
//				tempValue.get(j).setCount(theCount);
//			} else {
//				tempValue.add(entries);
//			}
//		}
//		stored.put(w.getWord(), tempValue);
//	} else {
//		ArrayList<UrlEntries> newList = new ArrayList<>();
//		newList.add(new UrlEntries(theCount, strURL));
//		stored.put(w.getWord(), newList);
//	}
//
//	System.out.println("Word ToString : " + " " + w.toString());
//}
//
//String realDomian = r.getDomain(strURL);
//String[] relativeLinks = r.getTheRelativeLinks(html, realDomian);
//String[] absoluteLinks = r.getTheAbsoluteLink(html, realDomian);
//ArrayList<String> internalLinks = r.getInternalLinks(relativeLinks, absoluteLinks);
//
//// sArrayList<String> externalLinks = r.getexternalLink(realDomian,
//// html);
//
//for (int i = 0; i < internalLinks.size(); i++) {
//	if (globalStore.contains(internalLinks.get(i))) {
//		internalLinks.remove(i);
//	}
//}
//
//for (int i = 0; i < internalLinks.size(); i++) {
//	if (globalStore.contains(internalLinks.get(i))) {
//
//	} else {
//		if (depth <= 10) {
//			depth += 1;
//			crawl1(internalLinks.get(i));
//		}
//	}
//}
//
//// for (int i = 0; i < externalLinks.size() && depth < 2; i++) {
//// depth +=1;
//// crawl(externalLinks.get(i), depth);
//// }
//
//}














// public void set(String strURL) throws IOException {
// Connection ce= new Connection();
//
// URL url = new URL(strURL);
// System.out.println("Url: " + url);
// HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
// String html = "";
// html = ce.getInputFromConnection(con, html);
//
// String[] wordArray = null;
//
// RegexUtility r = new MyRegexUtility();
//
// wordArray = r.getAllText(html);
// System.out.println("\nBody Tags for : " + strURL + "\n");
// r.getBetweenBodyTag(html);
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
//
// System.out.println("\n*****Text Inside div Tag for : " + strURL +
// "\n");
// // r.getBetweenSpecificTags(html, "div");
// System.out.println("\n*****Text Inside p Tag for : " + strURL +
// "\n");
// // r.getBetweenSpecificTags(html, "p");
// System.out.println("\n*****Text Inside span Tag for : " + strURL +
// "\n");
// //r.getBetweenSpecificTags(html, "span");
// System.out.println("\n*****Text Inside li Tag for : " + strURL +
// "\n");
// //r.getBetweenSpecificTags(html, "li");
// System.out.println("\n*****Text Inside label Tag for : " + strURL +
// "\n");
// // r.getBetweenSpecificTags(html, "label");
// System.out.println("\n*****Text Inside option Tag for : " + strURL +
// "\n");
// //r.getBetweenSpecificTags(html, "option");
// System.out.println("\n*****Text Inside td Tag for : " + strURL +
// "\n");
// // r.getBetweenSpecificTags(html, "td");
// System.out.println("\n*****Text Inside font Tag for : " + strURL +
// "\n");
// //r.getBetweenSpecificTags(html, "font");
//
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
// System.out.println("Text in an Alt Attribute: " + strURL + "\n");
// r.getBetweenAltAttribute(html);
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
// System.out.println("Text Inside Title Tag for : " + strURL + "\n");
// // r.getBetweenTitleTag(html);
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
//
//
// WordData wd = new WordData("WordDataBase.txt");
// ArrayList<String> checkList = wd.checkList(wordArray);
// Map<String, Integer> countMap = wd.setTheCount(wordArray, checkList);
//
//
// for (int i = 0; i < checkList.size(); i++) {
// int theCount = wd.searchTheCount(countMap, checkList.get(i));
// Word w = wd.createAWord(checkList.get(i), strURL, theCount);
//
// System.out.println("Word ToString : " + " " + w.toString());
// }
// String[] allAtags = r.getTheLinks(html);
// for (int i = 0; i < allAtags.length; i++) {
// set(allAtags[i]);
// }
// }
//

// --------------------------------------------------------------------------------

// System.out.println("/--------------------------------------------------------------------");
// System.out.println("\nThe Text: N Count");
// wordArray = r.getAllText(html);
//
// ArrayList<String> checkList = wd.checkList(wordArray);
// Map<String, Integer> countMap = wd.setTheCount(wordArray, checkList);
//
//
// for (int i = 0; i < checkList.size(); i++) {
// int theCount = wd.searchTheCount(countMap, checkList.get(i));
// Word w = wd.createAWord(checkList.get(i), strURL, theCount);
//
// System.out.println("Word ToString : " + " " + w.toString());
// }
//
// System.out.println("/--------------------------------------------------------------------");
//

// public WordData makeNAddWord(String word, String url, int count) throws
// IOException {
// wd = new WordData(pathFile);
// Word addWord = null;
// addWord = new Word();
// System.out.println("This : "+ addWord.toString() + "will be added.");
// //wd.insert(addWord);
// return wd;
// }

// public int setTheCount(String[] words, String url) throws IOException {
// int index = 0;
// int theCount = 0;
// int sizeOftheLoop = words.length;
//
// ArrayList<String> checkList = new ArrayList<>();
//
// ArrayList<String> wordsList = new ArrayList<>();
//
//
// for (int i = 0; i < sizeOftheLoop; i++) {
// wordsList.add(words[i]);
// }
//
// for (int i = 0; i < sizeOftheLoop; i++) {
// for (int j = i+1; j < sizeOftheLoop; j++) {
//
// if(checkList.contains(wordsList.get(j).toLowerCase())) {
//
// } else {
// if(wordsList.get(i).equalsIgnoreCase(words[j])) {
// checkList.add(words[j]);
// theCount++;
// wordsList.remove(j);
// System.out.println("Size of the WordList: " + wordsList.size());
// sizeOftheLoop--;
// //return theCount;
// }
// }
// }
// //add that word url and count to the HashMap
// //makeNAddWord(words[i], url, theCount);
// }
// return theCount;
// }
// ---------------------------------------------------------------------------------\

// System.out.println("\nBody Tags for : " + strURL + "\n");
// r.getBetweenBodyTag(html);
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
//
// System.out.println("\n*****Text Inside div Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "div");
// System.out.println("\n*****Text Inside p Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "p");
// System.out.println("\n*****Text Inside span Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "span");
// System.out.println("\n*****Text Inside li Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "li");
// System.out.println("\n*****Text Inside label Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "label");
// System.out.println("\n*****Text Inside option Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "option");
// System.out.println("\n*****Text Inside td Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "td");
// System.out.println("\n*****Text Inside font Tag for : " + strURL +
// "\n");
// r.getBetweenSpecificTags(html, "font");
//
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
// System.out.println("Text in an Alt Attribute: " + strURL + "\n");
// r.getBetweenAltAttribute(html);
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
// System.out.println("Text Inside Title Tag for : " + strURL + "\n");
// r.getBetweenTitleTag(html);
// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
////
//