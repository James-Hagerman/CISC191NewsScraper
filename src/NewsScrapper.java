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
* https://stackoverflow.com/questions/7048216/environment-variables-in-eclipse
* https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
* <<Add more references here>>
*
* Version: 2025-03-21
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Purpose: The reponsibility of NewsScrapper is ...
 *
 * NewsScrapper is-a ...
 * NewsScrapper is ...
 */
public class NewsScrapper
{
    public List<JSONObject> getArticles(String keyword, String fromDate, String toDate, String apiKey) throws IOException 
    {
//    	String apiKey = System.getenv("NEWS_API_KEY");
    	if (apiKey == null)
    	{
    		throw new IllegalStateException("API key not set.");
    	}
    	
    	
    	String endpoint = "https://newsapi.org/v2/everything" +
    			"?q=" + keyword +
    			"&from=" + fromDate +
    			"&to=" + toDate +
    			"&sortBy=publishedAt&pageSize=5" +
    			"&apiKey=" + apiKey;
    	
    	URL url = new URL(endpoint);
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	connection.setRequestMethod("GET");
    	connection.setRequestProperty("User-Agent", "Mozilla/5.0");
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    	
    	StringBuilder response = new StringBuilder();
    	String line;
    	while ((line = reader.readLine()) != null)
    	{
    		response.append(line);
    	}
    	reader.close();
    	
    	JSONObject json = new JSONObject(response.toString());
    	JSONArray articles = json.getJSONArray("articles");
    	
    	List<JSONObject> results = new ArrayList<>();
    	for (int i = 0; i < articles.length(); i++)
    	{
    		results.add(articles.getJSONObject(i));
    	}
    	
    	return results;
    }
    
    public static void main(String[] args) throws IOException 
    {
    	NewsScrapper scrapper = new NewsScrapper();
    	List<JSONObject> articles = scrapper.getArticles("tariffs", "2025-04-02", "2025-04-03", System.getenv("NEWS_API_KEY"));
    	
    	System.out.println("Top articles:");
    	for (JSONObject article : articles)
    	{
    		System.out.println("\nTitle: " + article.getString("title"));
            System.out.println("Source: " + article.getJSONObject("source").getString("name"));
            System.out.println("Date: " + article.getString("publishedAt"));
            System.out.println("Link: " + article.getString("url"));
    	}
    }
}
