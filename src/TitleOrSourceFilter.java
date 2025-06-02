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
*
* Version: 2025-05-17
*/

/**
 * Purpose: Matches articles whose source or title contains given keyword
 */
public class TitleOrSourceFilter implements ArticleFilter // A TitleOrSourceFilter IS-AN article filter
{
	private String keywordLower; // A title or source filter HAS-A keyword
	
	public TitleOrSourceFilter(String keyword) 
	{
		this.keywordLower = keyword.toLowerCase();
	}
	
	@Override
	public boolean matches(NewsArticle article)
	{
		// get article title and source
		String title = article.getTitle();
		String source = article.getSource();
		
		// Check if title isnt null isnt null and contains keyword
		if (title != null && title.toLowerCase().contains(keywordLower)) 
		{
			return true;
		}
		// Check if source isnt null and contains keyword
		if (source != null && source.toLowerCase().contains(keywordLower))
		{
			return true;
		}
		
		return false;
	}
}
