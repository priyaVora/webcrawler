package vora.priya.webcrawler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Word {
	private String word;
	private String url;

	private int count;
	
	public Word(String word, String url, int count) {
		super();
		this.count = count;
		this.word = word;
		this.url = url;
	}
	
	public Word() { 
		
	}
	public int serializedSize() {
		int serializedS = 255 + 1000 + 12;
		return serializedS;
	}
	public String serialize() { 
		String formatted =  String.format("%255s%1000s%12d",this.word, this.url, this.count);
		return formatted;
	}
	
	@Override
	public String toString() {
		return "Word: " + word + ", URL: " + url + ", Count: " + count+ ".";
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		
		
	}
	
}

	
