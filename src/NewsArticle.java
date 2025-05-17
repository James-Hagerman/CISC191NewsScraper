/**
* Lead Author(s):
* @author james Hagerman 
* 
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
* https://ioflood.com/blog/json-to-java-object/
*
* Version: 2025-05-17
*/

/**
 * Purpose: The reponsibility of NewsArticle is to hold data for a news article
 *
 * NewsArticle is-a data object
 * NewsArticle is used by the scrapper, database, and search engine
 */
public class NewsArticle
{
	public String title; // A NewsArticle HAS-A title
	public String source; // A NewsArticle HAS-A source
	public String url; // A NewsArticle HAS-A url
	public String publishedAt; // A NewsArticle HAS-A publication datetime
	
	// constructor
	public NewsArticle(String title, String source, String url, String publishedAt)
	{
		this.title = title;
		this.source = source;
		this.url = url;
		this.publishedAt = publishedAt;
	}
	
	// Getters
	public String getTitle()
	{
		return title;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public String getPublishedAt()
	{
		return publishedAt;
	}
	
	@Override
	public String toString() 
	{
		return title + "(source: " + source + ", date: " + publishedAt + ") url: " + url;
	}
}
