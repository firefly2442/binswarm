package binswarm.ui;
import java.awt.BorderLayout;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import binswarm.Networking;

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
        frame.addWindowListener(new WindowEventHandler());
        
        tabbedPane = new JTabbedPane();
        
        clusterPane = new JPanel(new BorderLayout());
        tabbedPane.addTab("Cluster Status", new ImageIcon("images/cluster_status.png"), clusterPane, "Shows network cluster status");
        networkPane = new JPanel(new BorderLayout());
        tabbedPane.addTab("Network Status", new ImageIcon("images/network_status.png"), networkPane, "Shows network traffic information");
        localcomputerPane = new JPanel(new BorderLayout());
        tabbedPane.addTab("Local Computer", new ImageIcon("images/local_computer.png"), localcomputerPane, "Setup and show bins for local computer");
        searchPane = new JPanel(new BorderLayout());
        tabbedPane.addTab("Search", new ImageIcon("images/search.png"), searchPane, "Search for files across the cluster");
        preferencesPane = new JPanel(new BorderLayout());
        tabbedPane.addTab("Preferences", new ImageIcon("images/preferences.png"), preferencesPane, "Setup BinSwarm preferences");
        helpPane = new JPanel(new BorderLayout());
        tabbedPane.addTab("Help", new ImageIcon("images/help.png"), helpPane, "Get help for running BinSwarm");
        aboutPane = new JPanel(new BorderLayout());
        tabbedPane.addTab("About", new ImageIcon("images/about.png"), aboutPane, "Shows information about BinSwarm");
        
        frame.add(tabbedPane);

        frame.setSize(800, 600);
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        //start threading to continuously update GUI components
        updateGUI task = new updateGUI();
        task.execute();
	}
}

class updateGUI extends SwingWorker<Void, Void>
{
	public updateGUI()
	{
		//Constructor
	}
	
	protected Void doInBackground() throws Exception
	{
		new Timer(1000, updateTable).start(); //update every second
		return null;
	}
	
	Action updateTable = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			GUI.clusterPane.removeAll(); //remove all old items
			
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("UUID");
			model.addColumn("IP Address");
			model.addColumn("Last Seen");
			
			JTable table = new JTable(model);
			
			JScrollPane clusterScroll = new JScrollPane(table);
			clusterScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			clusterScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			for (int i = 0; i < Networking.computers.size(); i++)
			{
				model.addRow(new Object[]{Networking.computers.get(i).uuid.toString(), Networking.computers.get(i).IPAddress, Networking.computers.get(i).returnHumanReadableTimestamp()});
			}
			
			GUI.clusterPane.add(clusterScroll, BorderLayout.CENTER);
			
			GUI.clusterPane.revalidate();
			GUI.clusterPane.repaint();
		}
	};
}

class WindowEventHandler extends WindowAdapter
{
	  public void windowClosing(WindowEvent evt)
	  {
		  TrayGUI.trayIcon.displayMessage("BinSwarm", "BinSwarm minimized to tray", TrayIcon.MessageType.INFO);
	  }
}
