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
*
* Version: 2025-05-24
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NewsScraperGUI extends JFrame 
{

	/**
	 * Purpose: Construct GUI components 
	 */
	public NewsScraperGUI() {
		setTitle("News Scraper Search");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		add(createTopPanel(), BorderLayout.NORTH);
		add(createTableScrollPane(), BorderLayout.CENTER);
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
		panel.add(new JTextField(15));
		// Create from date label and field 
		panel.add(new JLabel("From (YYYY-MM-DD):"));
		panel.add(new JTextField(10));
		// Create to date label and field
		panel.add(new JLabel("To (YYYY-MM-DD):"));
		panel.add(new JTextField(10));
		// Create search button
		panel.add(new JButton("Search"));
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
		DefaultTableModel table = new DefaultTableModel(columns, 0);
		// Create table using table model
		JTable result = new JTable(table);
		// Create scroll pane with table inside
		return new JScrollPane(result);
	}
	
    public static void main(String[] args) 
    {
        new NewsScraperGUI();
    }
}