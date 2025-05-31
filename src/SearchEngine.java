/**
* Lead Author(s):
* @author James Hagerman
* 
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
* https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
* https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
* https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
* https://docs.oracle.com/javase/8/docs/api/java/util/List.html
*
* Version: 2025-05-18
*/


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine
{
	private NewsDatabase db; // A search engine HAS-A news database 
	
	// Constructor
	public SearchEngine(NewsDatabase db) 
	{
		this.db = db;
	}
	
	/**
	 * Purpose: 
	 * @param keyword - keyword to search for
	 * @param fromDate - beginning date to search for
	 * @param toDate - ending date to search for
	 * @return List of news articles 
	 */
	public List<NewsArticle> search(List<ArticleFilter> filters, String keyword, LocalDate fromDate, LocalDate toDate) throws IOException
	{
		// Empty list to hold news article objects
		List<NewsArticle> results = new ArrayList<>();
		
		// iterate through each date in the passed range
		for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1))
		{
			// Initialize articles list outside try block 
			List<NewsArticle> dayArticles;
			
			try 
			{
			// load all articles for current date
				dayArticles = db.loadArticlesForDay(date);
			}
			catch (IOException e)
			{
				e.getMessage();
				continue;
			}
			
			// Iterate through each article on current date
			for (NewsArticle article : dayArticles) 
			{
				// assume article should be included
				boolean include = true;
				
				// loop through each filter in filter list
				for (ArticleFilter f : filters) 
				{
					// if filter isnt true dont include article
					if (!f.matches(article)) 
					{
						include = false;
						break;
					}
				}
				// if include is true add article to results
				if (include) 
				{
					results.add(article);
				}
			}
		}
		
		// return all results 
		return results;
	}
	
}
