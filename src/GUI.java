import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class GUI
{
	public static JFrame frame; //1st level
		public static JTabbedPane tabbedPane; //2nd level
			//3rd level
			public static JPanel clusterPane;
			public static JPanel networkPane;
			public static JPanel localcomputerPane;
			public static JPanel searchPane;
			public static JPanel preferencesPane;
			public static JPanel helpPane;
			public static JPanel aboutPane;
	
	public GUI()
	{
		//Constructor
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGUI();
            }
        });
	}
	
	private void createGUI()
	{
		frame = new JFrame("BinSwarm");
        frame.setLayout(new BorderLayout());
        
        //make sure the application doesn't exit upon closing the window
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        tabbedPane = new JTabbedPane();
        
        clusterPane = new JPanel();
        tabbedPane.addTab("Cluster Status", new ImageIcon("images/cluster_status.png"), clusterPane, "Shows network cluster status");
        networkPane = new JPanel();
        tabbedPane.addTab("Network Status", new ImageIcon("images/network_status.png"), networkPane, "Shows network traffic information");
        localcomputerPane = new JPanel();
        tabbedPane.addTab("Local Computer", new ImageIcon("images/local_computer.png"), localcomputerPane, "Setup and show bins for local computer");
        searchPane = new JPanel();
        tabbedPane.addTab("Search", new ImageIcon("images/search.png"), searchPane, "Search for files across the cluster");
        preferencesPane = new JPanel();
        tabbedPane.addTab("Preferences", new ImageIcon("images/preferences.png"), preferencesPane, "Setup BinSwarm preferences");
        helpPane = new JPanel();
        tabbedPane.addTab("Help", new ImageIcon("images/help.png"), helpPane, "Get help for running BinSwarm");
        aboutPane = new JPanel();
        tabbedPane.addTab("About", new ImageIcon("images/about.png"), aboutPane, "Shows information about BinSwarm");

        
        frame.add(tabbedPane);

        frame.setSize(640, 480);
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
}
