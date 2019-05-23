package project1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;


public class Spider
{
	Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	private  Integer page_count = 0;
	private  Integer pages_considered = 0;

 

  /**
   * Our main launching point for the Spider's functionality. Internally it creates spider legs
   * that make an HTTP request and parse the response (the web page).
   * 
   * @param url
   *            - The starting point of the spider
   * @param numPages
   *            - The maximum of number of pages to retrieve
   */
  public void search(String url, Integer max_number_of_pages_to_search, String RobotsDir)
  {

      String currentUrl = "";
      
      pagesToVisit.add(url);   // added the initial URL to the ToVisit list.
      
      while( (page_count < max_number_of_pages_to_search) && !pagesToVisit.isEmpty() )
      {
    	  ++pages_considered;
    	  //  be polite, wait 500 ms before visiting the next page
    	  try {
			Thread.sleep(500);
		  } catch (InterruptedException e) {
			; // do nothing on exception
		  }
    	  
          currentUrl = this.nextUrl();
			if (currentUrl.contains(RobotsDir)) {
				System.out.println(" ## sorry, can't go to this URL per robots.txt  = " + currentUrl);
			} else {
				SpiderLeg leg = new SpiderLeg();


				if (leg.crawl(currentUrl, page_count)) // Lots of stuff
														// happening here. Look
														// at the crawl method
				{
					++page_count;
					for (String onelink : leg.getLinks()) {
						String mytext = " ";
						if (pagesVisited.contains(onelink))
							mytext = " already visited!";
						if (pagesToVisit.contains(onelink))
							mytext = " already in the queue to visit.";
						if (mytext == " ") {
							this.pagesToVisit.add(onelink);
							mytext = " added to pagesToVisit";
						}
						System.out.println("  The link [" + onelink + "] " + mytext);
					} // end if crawl==true
				    project1.DocInfo ThisDoc = new project1.DocInfo();
				    ThisDoc.DocNumber = page_count;
				    ThisDoc.DocTitle = leg.getDocTitle();
				    ThisDoc.DocURL = currentUrl;
				    project1.projectdata.MyDocs.add(ThisDoc);
				} // end else currentURL okay
			} // end checking currentURL
      }
      System.out.println("\n**Done** Visited " + page_count + " valid web page(s)");

      
  }


  /**
   * Returns the next URL to visit (in the order that they were found). We also do a check to make
   * sure this method doesn't return a URL that has already been visited.
   * 
   * @return
   */
  private String nextUrl()
  {
      String nextUrl;
      do
      {
          nextUrl = this.pagesToVisit.remove(0);
      } while(this.pagesVisited.contains(nextUrl));
      this.pagesVisited.add(nextUrl);
      return nextUrl;
  }
  public Integer results()
  {
	  Integer mycount;

	  System.out.println("*******  Crawling/Visted Results *********");
	  Iterator<String> iter1 = pagesVisited.iterator();
	  mycount = 0;
	  while (iter1.hasNext()) {
		  ++mycount;
	      System.out.println("    "+mycount+": "+iter1.next());
	  }
	  System.out.println("*******  Broken Links *********");
	  Iterator<String> iter2 = projectdata.brokenLinks.iterator();
	  mycount = 0;
	  while (iter2.hasNext()) {
		  ++mycount;
	      System.out.println("    "+mycount+": "+iter2.next());
	  }
	  System.out.println("*******  Graphic/non-text Links *********");
	  Iterator<String> iter3 = projectdata.graphicLinks.iterator();
	  mycount = 0;
	  while (iter3.hasNext()) {
		  ++mycount;
	      System.out.println("    "+mycount+": "+iter3.next());
	  }
	  System.out.println("*******  URLs of pages with duplicate content *********");
	  Iterator<String> iter4 = projectdata.duplicateURLs.iterator();
	  mycount = 0;
	  while (iter4.hasNext()) {
		  ++mycount;
	      System.out.println("    "+mycount+": "+iter4.next());
	  }
	  return page_count;
  }
  
}
  