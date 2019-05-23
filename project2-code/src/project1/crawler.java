package project1;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import project1.SimilarityCalculate;


public class crawler
{
	static final int MAX_PAGES_TO_SEARCH = 50;
	static final String CSV_FILENAME = "c:\\temp\\termfreq.csv";
	
    public static void main(String[] args) throws IOException
    {
        //  tbd - implement arguments
    	//  arg0 = termfreq.csv output file
    	//  arg1 = number of pages to retrieve
        //  arg2 = filename of stop words
    	
    	String OUTPUT_CSV = CSV_FILENAME;
    	Integer MAX_PAGES = MAX_PAGES_TO_SEARCH;
    	
    	if ( args.length == 1) {
    		OUTPUT_CSV = args[0];
    	}
    	if (args.length == 2 ) {
    		OUTPUT_CSV = args[0];
    		MAX_PAGES = Integer.parseInt(args[1]);
    	}
    	
        //
    	for ( Integer i = 0; i < projectdata.MAXFILES; i++)
    		for ( Integer j = 0; j < projectdata.MAXWORDS; j++)
    			projectdata.TermFreq[i][j] = 0;
    	
    	// first, get the robots.txt file
		  Document doc = 
			         Jsoup.connect("http://s2.smu.edu/~fmoore/robots.txt").get();
		    doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
		    // get the HTML from the document, and retaining original new lines
		    String str = doc.html().replaceAll("\\\\n", "\n");
		    
		String[] str2 = str.split("\\n");  // split into lines
		String robots_dirname = "XXXXXXXXXXXXXXXXXXXXXXXX";
		
		for ( Integer i = 0; i < str2.length; i++)
		{
			if ( str2[i].toLowerCase().startsWith("disallow:")){   // look for the line starting with Disallow
				String[] dirnameline = str2[i].split("\\s+");
				robots_dirname = dirnameline[1];
				
			}
		}
		System.out.println("Found the disallows directory of = ("+robots_dirname+").");
	
		
    	
        Spider spider = new Spider();
        spider.search("http://lyle.smu.edu/~fmoore", MAX_PAGES_TO_SEARCH, robots_dirname);
        
        
        System.out.println("Done crawling, now for the results.   myDictionaryCount = "+projectdata.myDictionaryCount);

        Integer num_pages = 0;
        num_pages = spider.results();
        
        //  output the Term/Freq data to a file
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_CSV), "utf-8"));
        for ( Integer i = 0; i < projectdata.myDictionary.size(); i++)
        {
        	writer.write(i+", "+projectdata.myDictionary.get(i)+" ");
        	for ( Integer d = 0; d < num_pages; d++)  // d = 0 will be used for document frequency sum
        		writer.write(",  "+projectdata.TermFreq[d][i]);
        	writer.newLine();
        }       
        writer.close();
        System.out.println(" -- Summary of pages crawled, words will be in .csv file --");
        for(project1.DocInfo oneDoc : project1.projectdata.MyDocs)
        {
        	System.out.println(oneDoc.DocNumber+": "+oneDoc.DocURL+", TITLE="+oneDoc.DocTitle);
        }
        
        System.out.println("All done.  Review the results and the .csv file "+ OUTPUT_CSV);

        //My Codes:

        System.out.print("\n");

        Integer[][]  mydata = new Integer[projectdata.MyDocs.size()][projectdata.myDictionary.size()];

        for (Integer i = 0; i < mydata.length; i++) {
            System.arraycopy(projectdata.TermFreq[i], 0, mydata[i], 0, mydata[0].length);
        }

        double[] euclidian = SimilarityCalculate.CalEuclidian(mydata);
        double[][] normalization = SimilarityCalculate.CalNormal(mydata, euclidian);

        Divide.DivideDoc(normalization);
        System.out.print("\n");

        System.out.println("Enter the query: ");
        Scanner scan = new Scanner(System.in);
        String query = "";
        while(scan.hasNextLine()) {
            query = scan.nextLine();
            if (query.equalsIgnoreCase("stop")) {
                break;
            }
            else {
                if (!query.equals("")) {
                    String[] query_array = query.split("[^a-zA-Z]+");
                    //String[] query_array = {"moore", "smu"};
                    Query.HighestQuery(query_array, normalization);
                }
                else {
                    System.out.println("Your query is empty, please retype one.");
                }
            }
        }
        scan.close();

    }
}