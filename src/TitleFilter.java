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
 * Purpose: Matches articles whose title contains given keyword
 */
public class TitleFilter implements ArticleFilter // A title filter IS-AN article filter
{
	private String keywordLower; // A Title filter has a keyword
	
	public TitleFilter(String keyword) 
	{
		this.keywordLower = keyword.toLowerCase();
	}
	
	@Override
	public boolean matches(NewsArticle article)
	{
		// get article title
		String title = article.getTitle();
		
		// Check for null title
		if (title == null) 
		{
			return false;
		}
		
		// return true if title contains keyword
		return title.toLowerCase().contains(keywordLower);
	}
}
