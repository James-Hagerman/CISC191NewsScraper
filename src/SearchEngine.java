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
	public List<NewsArticle> search(String keyword, LocalDate fromDate, LocalDate toDate) throws IOException
	{
		// Empty list to hold news article objects
		List<NewsArticle> results = new ArrayList<>();
		// convert keyword to lowercase
		String searchKeyword = keyword.toLowerCase();
		
		// iterate through each date in the passed range
		for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1))
		{
			// load all articles for current date
			List<NewsArticle> dayArticles = db.loadArticlesForDay(date);
			
			// check each article for the keyword in the title or source 
			for (int i = 0; i < dayArticles.size(); i++) 
			{
				NewsArticle article = dayArticles.get(i);
				String title = article.getTitle().toLowerCase();
				String source = article.getSource().toLowerCase();
				
				// If title or source contains keyword include current article 
				if (title.contains(searchKeyword) || source.contains(searchKeyword))
				{
					results.add(article);
				}
			}
		}
		
		// return all results 
		return results;
	}
}
