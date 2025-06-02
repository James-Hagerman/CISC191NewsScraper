/**
* Lead Author(s):
* @author James Hagerman 
*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
* https://stackoverflow.com/questions/7048216/environment-variables-in-eclipse
* https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
* https://docs.oracle.com/javase/8/docs/api/java/net/URLEncoder.html
* https://newsapi.org/docs/endpoints/everything
* https://docs.oracle.com/javase/8/docs/api/java/nio/charset/StandardCharsets.html
* https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
* https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html
* https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html
* https://docs.oracle.com/javase/7/docs/api/java/net/URL.html
* https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
* https://docs.oracle.com/javase/8/docs/api/java/util/List.html
* https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
* https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
* https://www.tutorialspoint.com/org_json/org_json_jsonarray.htm
* https://www.tutorialspoint.com/org_json/org_json_quick_guide.htm
* "BufferedReader Vs Scanner Class In Java" https://www.naukri.com/code360/library/bufferedreader-vs-scanner-class-in-java
*
* Version: 2025-03-21
*/

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsScraper
{    
	/**
	 * Purpose: 
	 * @param date - The day to retrieve articles from 
	 * @param apiKey - key to connect to newsAPI
	 * @return A list of JSON objects of news articles 
	 */
    public List<JSONObject> getArticles(LocalDate date, String apiKey) throws IOException
    {
    	// List to hold articles 
    	List<JSONObject> allArticles = new ArrayList<>(); // getArticles HAS-A list of JSON object that are articles
    	
    	// Check if api key is missing 
    	if (apiKey == null) 
    	{
    		System.out.println("Error: API key is missing");
    		return allArticles;
    	}
    	
		// Get US source IDs
		String sourceIds = fetchUSSourceIDs(apiKey);
		
		// Split day into 6 windows to query to increase results
		LocalDateTime startOfDay = date.atStartOfDay();
		for (int window = 0; window < 4; window++)
		{
			// Calculate start and end time of current window
			LocalDateTime windowStart = startOfDay.plusHours(window * 6);
			LocalDateTime windowEnd = windowStart.plusHours(6);
			
			// URL encode time bounds
			String fromParam = URLEncoder.encode(windowStart.toString(), StandardCharsets.UTF_8);
			String toParam = URLEncoder.encode(windowEnd.toString(), StandardCharsets.UTF_8);
			
			// Create endpoint 
			String endpoint = "https://newsapi.org/v2/everything" +
					"?sources=" + sourceIds +
					"&from=" + fromParam +
					"&to=" + toParam +
					"&pageSize=100" +
					"&apiKey=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
			
			// Open connection and execute GET request with endpoint 
			HttpURLConnection conn = (HttpURLConnection)new URL(endpoint).openConnection();
			conn.setRequestMethod("GET");
			
			// Read the response body in try with resources to guarantee reader close
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)))
			{
    			StringBuilder response = new StringBuilder();
    			String line; 
    			while ((line = reader.readLine()) != null) 
    			{
    				response.append(line);
    			}
    			
    			// Parse the JSON and append each article to the list 
    			try 
    			{
	    			JSONArray articlesArray = new JSONObject(response.toString()).getJSONArray("articles");
	    			for (int i = 0; i < articlesArray.length(); i++) 
	    			{
	    				allArticles.add(articlesArray.getJSONObject(i));
	    			}
    			}
    			catch (JSONException je) 
    			{
    				throw new IOException("bad JSON response: " + je.getMessage(), je);
    			}
    			}
    		}
    	
    	
    	return allArticles;
    }
    
    /**
     * Purpose: Fetch all the source IDs for US sources for use in API endpoint
     * @param apiKey - key that gives access to the newsAPI
     * @return String of comma separated source IDs
     */
    private String fetchUSSourceIDs(String apiKey) throws IOException 
    {	
    	// Endpoint to retrieve sources
    	String url = "https://newsapi.org/v2/sources?country=us&apiKey=" +
    			URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
    	
    	// Open connection and get 
    	HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
    	conn.setRequestMethod("GET");
    	
        // check HTTP status code
        int status = conn.getResponseCode();
        
        if (status == 429) {
            // Too Many Requests
            throw new IOException("RATE_LIMIT");
        } else if (status != 200) {
            // throw an IOException with details
            throw new IOException("HTTP " + status + ": " + conn.getResponseMessage());
        }
    	
    	// Read the response body
    	try (BufferedReader reader = new BufferedReader(
    			new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
    			))
    	{
	    	// Create a string builder
	    	StringBuilder sourcesString = new StringBuilder();
	    	String line;
	    	
	    	// loop while the line is not null
	    	while ((line = reader.readLine()) != null) 
	    	{
	    		// add current line onto string builder
	    		sourcesString.append(line);
	    	}
	    	// close reader
	    	reader.close();
	    	
	    	// parse JSON and gather the source IDs and build comma separated list 
	    	try 
	    	{
			    JSONArray sourceArray = new JSONObject(sourcesString.toString()).getJSONArray("sources");
			    StringBuilder sourcesIds = new StringBuilder();
			    for (int i = 0; i < sourceArray.length(); i++) 
			    {
			    	if (i > 0)
			    	{
			    		sourcesIds.append(',');
			    	}
			    	String id = sourceArray.getJSONObject(i).getString("id");
			    	sourcesIds.append(id);
			    }
			    
			    return sourcesIds.toString();
	    	}
	    	catch (JSONException je)
	    	{
	    		throw new IOException("Bad JSON in Source list: " + je.getMessage(), je);
	    	}
    	}
    }
}




