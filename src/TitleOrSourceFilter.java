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
 * Purpose: Matches articles whose source or title contains given keyword
 */
public class TitleOrSourceFilter implements ArticleFilter
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
