package vora.priya.webcrawler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordData {
	private RandomAccessFile file;

	private int nextOffset = 8;

	public WordData(String path) throws IOException {
		boolean readOffsetFromFile = false;
		if (new File(path).exists()) {
			readOffsetFromFile = true;
		}

		file = new RandomAccessFile(path, "rw");
		if (readOffsetFromFile) {
			nextOffset = file.readInt();
		} else {
			file.writeInt(nextOffset);
		}
	}
	public int searchTheCount(Map<String, Integer> countMap, String word) { 
		int theCount = countMap.get(word);
		return theCount;
	}
	
	public ArrayList<String> checkList(List<String> wordArray) { 
		ArrayList<String> checkList = new ArrayList<>();
		for (int i = 0; i < wordArray.size(); i++) {

			if (checkList.contains(wordArray.get(i))) {

			} else {
				checkList.add(wordArray.get(i));
			}
		}
		return checkList;
	}

	public Map<String, Integer> setTheCount(List<String> words, ArrayList<String> checkList) {

		Map<String, Integer> countMap = new HashMap<>();
		
		System.out.println(" ");
		for (int i = 0; i < checkList.size(); i++) {
			// System.out.println((i+1) + "Size of the check list : " +
			// checkList.size());
			int count = 0;
			for (int j = 0; j < words.size(); j++) {
				if (checkList.get(i).equals(words.get(j))) {
					count++;
				}
			}
			countMap.put(checkList.get(i), count);
			int countOftheWord = countMap.get(checkList.get(i));
			System.out.println("Word: " + checkList.get(i) + " : " + countOftheWord);
		}
		return countMap;
	}
	
	public Word createAWord(String word, String strURL, int count) {
		Word w = new Word();
		w.setWord(word);
		w.setUrl(strURL);
		w.setCount(count);
		return w;

	}
	
	
	public void insert(Word w) throws IOException {

		byte[] buffer = w.serialize().getBytes();
		if (buffer.length != w.serializedSize()) {
			throw new IllegalArgumentException(
					"serialized size is not as promised. " + buffer.length + "!= " + w.serialize());
		}
		file.seek(nextOffset);
		file.write(buffer);
		nextOffset += buffer.length;
		updateNextOffSet();

	}

	private void updateNextOffSet() throws IOException {
		file.seek(0);
		file.writeInt(nextOffset);
		file.seek(nextOffset);
	}

	public Word read(int index) throws IOException, ClassNotFoundException {
		int totalSizeOfContact = 1267;
		int findingIndex = ((index * totalSizeOfContact) + 8);
		file.seek(findingIndex);
		byte[] getInfoArray = new byte[totalSizeOfContact];
		file.read(getInfoArray);
		
		String theWord = new String(getInfoArray, "UTF-8");
		String word = theWord.substring(0, 255).trim().replace(" ", "");
		String url = theWord.substring(255, 1255).trim().replace("", "");
		String count = theWord.substring(1255,1267).trim().replace(" ", "");
		
		int numberCount = Integer.parseInt(count);
		
		Word w = new Word(word, url,numberCount);
		return w;
	}

}
