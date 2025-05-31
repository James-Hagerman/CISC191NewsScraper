/**
* Lead Author(s):
* @author James Hagerman

*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
* https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
* https://www.tutorialspoint.com/how-to-create-defaulttablemodel-which-is-an-implementation-of-tablemodel
* https://www.tutorialspoint.com/how-to-change-each-column-width-of-a-jtable-in-java
* https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
* https://docs.oracle.com/javase/7/docs/api/javax/swing/JScrollPane.html
* https://www.geeksforgeeks.org/java-jscrollpane/
* https://stackoverflow.com/questions/55523906/how-to-give-error-message-when-user-input-wrong-format-of-date-in-java8
* https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeParseException.html
*
* Version: 2025-05-28
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NewsScraperGUI extends JFrame 
{
	private JTextField keywordField; // A NewsScraperGUI HAS-A keyword field
	private JTextField fromField; // A NewsScraperGUI HAS-A from date field
	private JTextField toField; // A NewsScraperGUI HAS-A to date field
	private JButton searchButton; // A NewsScraperGUI HAS-A search button
	private JComboBox<String> searchBy; // A NewsScraperGUI HAS-A search by modifier
	
	private DefaultTableModel tableModel; // A NewsScraperGUI HAS-A 2D array to store data
	private JTable table; // A NewsScraperGUI HAS-A view of the table data
	private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMM d, YYYY 'at' hh:mm a"); // A NewsScraperGUI HAS-A datetime formatter
	
	private NewsScraper scraper; // A NewsScraperGUI HAS-A News scraper for fetching articles
	private NewsDatabase database; // A NewsScraperGUI HAS-A database for fetching stored articles
	private SearchEngine engine; // A NewsScraperGUI HAS-A SearchEngine for searching for the keyword
	

	/**
	 * Purpose: Construct GUI components 
	 */
	public NewsScraperGUI() 
	{
		// title of window 
		setTitle("News Scraper Search");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1200, 600);
		
		// Initialize scraper, database, and search engine
		createBackend();
		// Add user input panel 
		add(createTopPanel(), BorderLayout.NORTH);
		// Add result table 
		add(createTableScrollPane(), BorderLayout.CENTER);
		// connect search listener
		searchListener();
		
		// show window 
		setVisible(true);
	}
	
	/**
	 * Purpose: Create top panel for user entry  
	 * @return top panel 
	 */
	private JPanel createTopPanel() 
	{
		// Create Panel 
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		// Create keyword label and field
		panel.add(new JLabel("Keyword Search:"));
		keywordField = new JTextField(15);
		panel.add(keywordField);
		
		// Create from date label and field 
		panel.add(new JLabel("From (YYYY-MM-DD):"));
		fromField = new JTextField(10);
		panel.add(fromField);
		
		// Create to date label and field
		panel.add(new JLabel("To (YYYY-MM-DD):"));
		toField = new JTextField(10);
		panel.add(toField);
		
		// Create search by drop down
		panel.add(new JLabel("Search By:"));
		String[] options = {"Title", "Source", "Both"};
		searchBy = new JComboBox<>(options);
		panel.add(searchBy);
		
		// Create search button
		searchButton = new JButton("Search");
		panel.add(searchButton);
		
		return panel;
	}
	
	/**
	 * Purpose: Create a scroll pane containing article table 
	 * @return the scroll pane 
	 */
	private JScrollPane createTableScrollPane() 
	{
		// column headers
		String[] columns = {"Title", "Source", "Date", "Link"};
		// Create table model with column headers
		tableModel = new DefaultTableModel(columns, 0);
		// Create table using table model
		table = new JTable(tableModel);
		// Create scroll pane with table inside
		return new JScrollPane(table);
	}
	
	/**
	 * Purpose: Set preferred width on columns in the table
	 */
	private void setColumns() 
	{
		// Allow columns to auto resize when window size changes
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getColumnModel().getColumn(0).setPreferredWidth(600); // Title column
		table.getColumnModel().getColumn(1).setPreferredWidth(150); // Source column
		table.getColumnModel().getColumn(2).setPreferredWidth(150); // Date column
		table.getColumnModel().getColumn(3).setPreferredWidth(300); // Link column
	}
	
	/**
	 * Purpose: Initialize backend objects NewsScraper, NewsDatabase, and SearchEngine
	 */
	private void createBackend() 
	{
		try 
		{
			scraper = new NewsScraper();
			database = new NewsDatabase("news-storage", scraper);
			engine = new SearchEngine(database);
		}
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(this, "Error initializing backend: " + e.getMessage(),"Initialization Error",  JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	/**
	 * Purpose: Attach action listener to the search button
	 */
	private void searchListener() 
	{
		searchButton.addActionListener(new ActionListener() 
				{
			public void actionPerformed(ActionEvent e) 
			{
				search();
			}
		});
	}
	
	/**
	 * Purpose: Search through articles when the user clicks the search button 
	 */
	private void search() 
	{
		// get user input text 
		String keyword = keywordField.getText().trim();
		String from = fromField.getText().trim();
		String to = toField.getText().trim();
		
		// create list of filters based on the search by choice
		List<ArticleFilter> filters = new ArrayList<>();
		String choice = (String) searchBy.getSelectedItem();
		
		// add filters if keyword is not empty
		if (!keyword.isEmpty()) 
		{
			switch (choice)
			{
				case "Title":
					filters.add(new TitleFilter(keyword));
					break;
				case "Source":
					filters.add(new SourceFilter(keyword));
					break;
				case "Both":
					filters.add(new TitleOrSourceFilter(keyword));
					break;
				default:
					// Fallsback to a title search
					filters.add(new TitleFilter(keyword));
			}
		}
		
		try 
		{
			// parse dates
			LocalDate fromDate = LocalDate.parse(from);
			LocalDate toDate = LocalDate.parse(to);
			
			// Perform search 
			List<NewsArticle> searchList = engine.search(filters, keyword, fromDate, toDate);
			
			// handle no results
			if (searchList.isEmpty()) 
			{
				String[][] data = {{"No articles found", "", "", ""}};
				tableModel.setDataVector(data, new String[]{"Title", "Source", "Date", "Link"});
			}
			else 
			{
				// 2D array of strings to fill in table 
				String[][] data = new String[searchList.size()][4];
				// iterate through articles 
				for (int i = 0; i < searchList.size(); i++)
				{
					NewsArticle article = searchList.get(i);
                    data[i][0] = article.getTitle(); // title column
                    data[i][1] = article.getSource(); // source column
                    // parse timestamp and format it 
					OffsetDateTime prettyDate = OffsetDateTime.parse(article.getPublishedAt());
					data[i][2] = prettyDate.format(dateTimeFormat); // datetime column
					
                    data[i][3] = article.getUrl(); // link column
				}
				// set table model with result data 
				tableModel.setDataVector(data, new String[]{"Title", "Source", "Date", "Link"});
			}
			// set column widths after loading new data
			setColumns();
		}
        catch (java.time.format.DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter dates in YYYY-MM-DD format.",
                "Invalid Date",
                JOptionPane.ERROR_MESSAGE
            );
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "I/O error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
	
    public static void main(String[] args) 
    {
        new NewsScraperGUI();
    }
}