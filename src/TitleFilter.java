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
 * Purpose: Matches articles whose title contains given keyword
 */
public class TitleFilter implements ArticleFilter 
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
