/**
* Lead Author(s):
* @author James Hagerman
*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
* https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
* https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html
* https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html
* https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html
* https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
* https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
* https://docs.oracle.com/javase/8/docs/api/java/util/List.html
* https://www.tutorialspoint.com/org_json/org_json_jsonarray.htm
* https://www.tutorialspoint.com/org_json/org_json_quick_guide.htm
* https://docs.oracle.com/javaee/7/api/javax/json/JsonException.html
*
* Version: 2025-05-17
*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsDatabase
{
	private Path storageDirectory; // A NewsDatabase HAS-A directory where JSON files are stored
	private NewsScraper scrapper; // A NewsDatabase HAS-A helper method for fetching articles from the api
	private String apiKey = System.getenv("NEWS_API_KEY"); // A NewsDatabase HAS-AN API key for connecting to the api
	
	// Constructor 
	public NewsDatabase(String directory, NewsScraper scrapper) throws IOException
	{
		this.storageDirectory = Paths.get(directory);
		this.scrapper = scrapper;
		// creates storage directory only if it doesnt exist
		Files.createDirectories(storageDirectory);	
	}
	
	public List<NewsArticle> loadArticlesForDay(LocalDate date) throws IOException
	{	
		// create path to json file for specific day
		Path file = storageDirectory.resolve(date.toString() + ".json");
		
		// Check if file exists
		if (Files.exists(file))
		{
			long size = Files.size(file);
			// delete file if it is empty
		    if (size == 0) {
		        Files.delete(file);
		    } 
		    // delete file if it only contains empty braces
		    else 
		    {
		        String newsText = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
		        if (newsText.trim().equals("[]")) {
		            Files.delete(file);
		        }
		    }
		}
		
		// check if day specific file doesnt exist
		if (!Files.exists(file)) 
		{
			// retrieve JSONObjects on specified date
			List<JSONObject> rawJSON = scrapper.getArticles(date, apiKey);
			
			// Check if json is empty to prevent writing an empty file 
			if (rawJSON.isEmpty())
			{
				return Collections.emptyList();
			}
			
			// wrap list in JSONArray and write bytes
			Files.write(file, new JSONArray(rawJSON).toString().getBytes());
		}
		
		List<NewsArticle> output = new ArrayList<>();
		
		// read file as a string 
		String newsText = new String(Files.readAllBytes(file));
		
		JSONArray newsArray;
		
		// parse as a JSON array
		try {
			newsArray = new JSONArray(newsText);
		}
		catch (JSONException je)
		{
			Files.deleteIfExists(file);
			throw new IOException("Bad JSON for date: " + date + ": " + je.getMessage(), je);
		}
		
		// convert each JSONObject into a NewsArticle
		for (int i = 0; i < newsArray.length(); i++)
		{
			try {
				JSONObject obj = newsArray.getJSONObject(i);
				
				// extract all four fields 
				String title = obj.getString("title");
				String url = obj.getString("url");
				String publishedAt = obj.getString("publishedAt");
				String source = obj.getJSONObject("source").getString("name");
				
				output.add(new NewsArticle(title, source, url, publishedAt));
			}
			catch (JSONException je)
			{
				// skip bad json articles
				continue;
			}
		}
		
		return output;
	}
}
