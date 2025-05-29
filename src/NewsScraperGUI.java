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
* programming
*
* Version: 2025-05-24
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;

public class NewsScraperGUI extends JFrame 
{
	private JTextField keywordField;
	private JTextField fromField;
	private JTextField toField;
	private JButton searchButton;
	private DefaultTableModel tableModel;
	private JTable table;
	
	private NewsScraper scraper;
	private NewsDatabase database;
	private SearchEngine engine;
	

	/**
	 * Purpose: Construct GUI components 
	 */
	public NewsScraperGUI() {
		setTitle("News Scraper Search");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1200, 600);
		createBackend();
		add(createTopPanel(), BorderLayout.NORTH);
		add(createTableScrollPane(), BorderLayout.CENTER);
		searchListener();
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
		panel.add(new JLabel("Keyword:"));
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
		// Set columns widths
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getColumnModel().getColumn(0).setPreferredWidth(600);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(300);
		// Create scroll pane with table inside
		return new JScrollPane(table);
	}
	
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
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(1);
		}
	}
	
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
	
	private void search() 
	{
		tableModel.setRowCount(0);
		String keyword = keywordField.getText().trim();
		String from = fromField.getText().trim();
		String to = toField.getText().trim();
		
		try 
		{
			LocalDate fromDate = LocalDate.parse(from);
			LocalDate toDate = LocalDate.parse(to);
			
			List<NewsArticle> list = engine.search(keyword, fromDate, toDate);
			
			if (list.isEmpty()) 
			{
				tableModel.addRow(new Object [] {"No Articles Found", "", "", ""});
				
			}
				else 
				{
					for (NewsArticle a : list) 
					{
						tableModel.addRow(new Object[]{
								a.getTitle(), 
								a.getSource(),
								a.getPublishedAt().toString(),
								a.getUrl()
						});
					}
				}
			
		}
	catch (IOException e) 
	{
		tableModel.addRow(new Object[] {"I/O error", "", "", e.getMessage()});
	}
	
		
	}
	
    public static void main(String[] args) 
    {
        new NewsScraperGUI();
    }
}