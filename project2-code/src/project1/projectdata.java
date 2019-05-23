package project1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class projectdata {
	static Integer MAXFILES = 50;
	static Integer MAXWORDS = 2000;
	public static List<String> brokenLinks = new LinkedList<String>();
	public static List<String> graphicLinks = new LinkedList<String>();
	public static List<String> duplicateURLs = new LinkedList<String>();
	public static ArrayList<String> myDictionary = new ArrayList<>();  
	public static Integer myDictionaryCount = 0;
	public static Integer[][] TermFreq = new Integer[MAXFILES][MAXWORDS]; 
	
	public static List<project1.DocInfo> MyDocs = new LinkedList<project1.DocInfo>();

	public static List<String[]> DocumentContent = new ArrayList<String[]>();
}
