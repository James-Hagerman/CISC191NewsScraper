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
* Version: 2025-05-31
*/

/**
 * Purpose: Matches articles whose source contains given keyword
 */
public class SourceFilter implements ArticleFilter
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
