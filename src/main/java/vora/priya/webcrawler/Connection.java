package vora.priya.webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

	
	public HttpURLConnection openConnection(String strURL) throws IOException { 	
		URL url = new URL(strURL);
		System.out.println("Url: " + url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(1000);
		return con;
	}
	
	public String getInputFromConnection(HttpURLConnection con, String html) throws IOException { 
		try {
			try (InputStream in = con.getInputStream()) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
					while (br.ready()) {
						html += br.readLine();
						//System.out.println("HTML: " + html);
					}
				}
			}
		} finally {
			con.disconnect();
		}
		return html;
	}

	public String[] findSpecificTagFromHTML(String html, String findTag)  {
		RegexUtility regexUtil = new MyRegexUtility();
		String[] array = regexUtil.getHTMLTagsContents(html, findTag);
		for (String string : array) {
			System.out.println(string);
		}
		return array;
	}
	
	
}
