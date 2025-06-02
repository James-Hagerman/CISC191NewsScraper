
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
 * Purpose: Matches articles whose source contains given keyword
 */
public class SourceFilter implements ArticleFilter // A source Filter IS-AN article filter
{
	private String keywordLower; // A source filter HAS-A keyword 
	
	public SourceFilter(String sourceString)
	{
		this.keywordLower = sourceString.toLowerCase();
	}
	
	@Override
	public boolean matches(NewsArticle article) 
	{
		// get article source
		String source = article.getSource();
		
		// check if source is null
		if (source == null) 
		{
			return false;
		}
		
		// Return true if source contains the keyword
		return source.toLowerCase().contains(keywordLower);
	}
}
