/**
* Lead Author(s):
* @author james; student ID
* @author Full name; student ID
* <<Add additional lead authors here>>
*
* Other Contributors:
* Full name; student ID or contact information if not in class
* <<Add additional contributors (mentors, tutors, friends) here, with contact information>>
*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
*
* <<Add more references here>>
*
* Version: 2025-05-18
*/

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class Main
{
	public static void main(String[] args) throws IOException
	{
		// Pull in arguments 
		String keyword = args[0];
		LocalDate from = LocalDate.parse(args[1]);
		LocalDate to = LocalDate.parse(args[2]);
		
		// Create scrapper, database, and search engine objects
		String storageDirectory = "news-storage";
		NewsScrapper scrapper = new NewsScrapper();
		NewsDatabase db = new NewsDatabase(storageDirectory, scrapper);
		SearchEngine engine = new SearchEngine(db);
		
		// perform search
		List<NewsArticle> results = engine.search(keyword, from, to);
		
		// Print output 
		System.out.println("Found " + results.size() + " articles for '" + keyword + "' from " + from + " to " + to + ":\n");
		for (NewsArticle article : results) 
		{
            System.out.println("Title:     " + article.getTitle());
            System.out.println("Source:    " + article.getSource());
            System.out.println("Date:      " + article.getPublishedAt());
            System.out.println("URL:       " + article.getUrl());
            System.out.println();
		}
	}
}
