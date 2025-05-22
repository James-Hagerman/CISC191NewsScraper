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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewsDatabase
{
	private Path storageDirectory; // A NewsDatabase HAS-A directory where JSON files are stored
	private NewsScrapper scrapper; // A NewsDatabase HAS-A helper method for fetching articles from the api
	private String apiKey = System.getenv("NEWS_API_KEY"); // A NewsDatabase HAS-AN API key for connecting to the api
	
	// Constructor 
	public NewsDatabase(String directory, NewsScrapper scrapper) throws IOException
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
		
		// check if day specific file doesnt exist
		if (!Files.exists(file)) 
		{
			// retrieve JSONObjects on specified date
			List<JSONObject> rawJSON = scrapper.getArticles(date, apiKey);
			// wrap list in JSONArray and write bytes
			Files.write(file, new JSONArray(rawJSON).toString().getBytes());
		}
		
		List<NewsArticle> output = new ArrayList<>();
		
		// read file as a string 
		String newsText = new String(Files.readAllBytes(file));
		
		// parse as a JSON array
		JSONArray newsArray = new JSONArray(newsText);
		
		// convert each JSONObject into a NewsArticle
		for (int i = 0; i < newsArray.length(); i++)
		{
			JSONObject obj = newsArray.getJSONObject(i);
			
			// extract all four fields 
			String title = obj.getString("title");
			String url = obj.getString("url");
			String publishedAt = obj.getString("publishedAt");
			String source = obj.getJSONObject("source").getString("name");
			
			output.add(new NewsArticle(title, source, url, publishedAt));
		}
		
		return output;
	}
}
