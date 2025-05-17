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
* Version: 2025-05-17
*/

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * Purpose: The reponsibility of NewsArticleTest is ...
 *
 * NewsArticleTest is-a ...
 * NewsArticleTest is ...
 */
public class NewsArticleTest
{
	@Test
	void testJsonMapping()
	{
		JSONObject rawJson = new JSONObject()
				.put("title", "Example Title")
				.put("url", "https://ExampleURL.com")
				.put("publishedAt", "2025-01-01")
				.put("source", new JSONObject().put("name", "CBSNews"));
	
	
	NewsArticle test = new NewsArticle(
			rawJson.getString("title"),
			rawJson.getJSONObject("source").getString("name"),
			rawJson.getString("url"),
			rawJson.getString("publishedAt")
			);
	
	
	assertEquals("Example Title", test.title);
	assertEquals("CBSNews", test.source);
	assertEquals("https://ExampleURL.com", test.url);
	assertEquals("2025-01-01", test.publishedAt);
	}
}
