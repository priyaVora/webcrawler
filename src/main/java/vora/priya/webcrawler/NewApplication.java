package vora.priya.webcrawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NewApplication {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		Scanner scan = new Scanner(System.in);
		String input = null;
		Map<String, ArrayList<UrlEntries>>loaded = null;
		
				
			if(new File("Word_Data").exists()) { 
				try (FileInputStream theInput = new FileInputStream("Word_Data")) {
					try (ObjectInputStream objIn = new ObjectInputStream(theInput)) {
						Object raw = objIn.readObject();
						loaded = ((Map<String, ArrayList<UrlEntries>>) raw);
					}
				}	
			} 		
			
			for (int i = 0; i < 1; i++) {
				System.out.println("Enter a word....");
				input = scan.nextLine();
				
			}
					
				ArrayList<UrlEntries> newSdfsf = loaded.get(input);
			
				for (int i = 0; i < newSdfsf.size(); i++) {
					System.out.println("Value of Word : " + newSdfsf.get(i).getCount() +  " " + newSdfsf.get(i).getUrl());
					
				}
		
	}
}
