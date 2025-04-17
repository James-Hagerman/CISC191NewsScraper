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
* Version: 2025-04-16
*/
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Purpose: The reponsibility of NewsScrapperTests is ...
 *
 * NewsScrapperTests is-a ...
 * NewsScrapperTests is ...
 */
public class NewsScrapperTests
{
    @Test
    void testGetArticlesReturnsResults() throws IOException {
        NewsScrapper scraper = new NewsScrapper();
        List<JSONObject> articles = scraper.getArticles("technology", "2025-04-01", "2025-04-10", System.getenv("NEWS_API_KEY"));

        assertNotNull(articles);
        assertFalse(articles.isEmpty());

        for (JSONObject article : articles) {
            assertTrue(article.has("title"));
            assertTrue(article.has("publishedAt"));
            assertTrue(article.has("url"));
            assertTrue(article.has("source"));
        }
    }

    @Test
    void testMissingApiKeyThrowsException() {
        NewsScrapper scraper = new NewsScrapper();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            scraper.getArticles("tech", "2025-04-01", "2025-04-10", null);
        });

        assertEquals("API key not set.", exception.getMessage());
    }
  
}
