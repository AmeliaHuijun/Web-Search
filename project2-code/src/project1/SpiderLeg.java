package project1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg
{
  // We'll define the name for this crawler.
  private static final String USER_AGENT = "fmoore-cse7337-smu";
  private List<String> links = new LinkedList<String>();
  private Document htmlDocument;
  private String DocTitle;
  private static ArrayList<String> dupfilehash = new ArrayList<>();  

  /**
   * This performs all the work. It makes an HTTP request, checks the response, and then gathers
   * up all the links on the page. Perform a searchForWord after the successful crawl
   * 
   * @param url
   *            - The URL to visit
   * @return whether or not the crawl was successful
   * @throws NoSuchAlgorithmException
   */
  public boolean crawl(String url, Integer pagenumber) 
  { Integer localcount;
      
          Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
          try {
        	  htmlDocument = connection.get();
         
          if( (connection.response().statusCode() == 200)  // 200 is the HTTP OK status code
               ||
             ( connection.response().statusCode() == 302 ) ) // 302 indicates it was moved but okay.
          {
              System.out.println("\n**Visiting #"+pagenumber+" ** Received web page at " + url);
    	      System.out.println(" ---- title: "+htmlDocument.title());
    	      DocTitle = htmlDocument.title();
          }
  //        if(!connection.response().contentType().contains("text/html"))
  //        {
  //            System.out.println("**Failure** Retrieved something other than HTML");
  //            return false;
  //        }
          Elements linksOnPage = htmlDocument.select("a[href]");
          System.out.println("Found (" + linksOnPage.size() + ") links");
          
          String thisfiletext = htmlDocument.body().text();
          for ( int i = 0; i < dupfilehash.size(); i++)
          {
        	  if ( thisfiletext.equals(dupfilehash.get(i))) 
        	  {
           		  System.out.println("  hey -- this URL has the same content (i.e. duplicate) of a previously crawled page");
           		  projectdata.duplicateURLs.add(url);
        		  return false;
        	  }
          }
          dupfilehash.add(htmlDocument.body().text());  // save the "hash" value for this file for later comparison
          // for each link on the page, only add to list if it is local and .htm .html .txt or .php
          
          localcount = 0;
          for(Element link : linksOnPage)
          {       String onelink = link.absUrl("href");       
        	      System.out.println("Found link:  "+onelink );
        	      
                  if ( onelink.startsWith( "http://lyle.smu.edu/~fmoore" )
                		  || onelink.startsWith("http://s2.smu.edu/~fmoore")
                		  || onelink.startsWith("https://lyle.smu.edu/~fmoore") 
                		  || onelink.startsWith("https://s2.smu.edu/~fmoore"))
                  {
                	  if (onelink.endsWith(".htm") || onelink.endsWith(".html")  || onelink.endsWith(".txt")
                			   || onelink.endsWith(".php") )
                			  {
                		        	  ++localcount;
                		        	  this.links.add(onelink);
                			  }
                	  else
                	  {
                		  if ( !projectdata.graphicLinks.contains(onelink) ) 
                		  {
                			  projectdata.graphicLinks.add(onelink);
                		  }
                	  }
                		        	
                  }
          }
          System.out.println("Found ("+linksOnPage.size()+ ") links, but only ("+localcount+") are local.");
          
          String []strarray = htmlDocument.body().text().split("[^a-zA-Z]+");
          projectdata.DocumentContent.add(strarray);
          
          Boolean foundWord;
          
          // for each word, find it in the dictionary
          for ( Integer i = 0; i < strarray.length; i++)
          {
        	  String oneword = strarray[i].toLowerCase();

              /*if (pagenumber == 21 || pagenumber == 20) {
                  System.out.println("     word "+i+" = "+oneword);
              }*/

  //      	  System.out.println("     word "+i+" = "+oneword);
        	  foundWord = false;
        	  for ( Integer dictpos = 0; dictpos < projectdata.myDictionary.size(); dictpos++) 
        	  {
        		  if ( oneword.equals(projectdata.myDictionary.get(dictpos)) )
        		  {
        			  // found the word, increment the count for the document in TermFreq
       // 			  System.out.println("  ### found the word ("+oneword+"), now update TermFreq ["+pagenumber+"]["+dictpos+"]");
        			  ++projectdata.TermFreq[pagenumber][dictpos];
        			  foundWord = true;
        		  }
        	  }
        	  // if the word was not found, add it to the dictionary
        	  if (!foundWord)
        	  {
        //		  System.out.println("  not found, ready to add ("+oneword+
        //				  " at position ["+pagenumber+"]["+projectdata.myDictionary.size()+"]  okay?");
        		  projectdata.myDictionary.add(oneword);
        		  projectdata.TermFreq[pagenumber][projectdata.myDictionary.size() - 1] = 1;
        		  ++projectdata.myDictionaryCount;
        	  }
          }
          return true;
          }
      catch(IOException ioe)
      {
          // We were not successful in our HTTP request
    	  projectdata.brokenLinks.add(url);
    	  return false;
      }
  }



  public List<String> getLinks()
  {

	  return this.links;
  }

  public String getDocTitle()
  {
	  return this.DocTitle;
  }
}

