package binswarm.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import binswarm.config.LocationSettings;

public class LocalComputerGUI {
	public LocalComputerGUI() {
		//constructor
	}
	
	public static JPanel setupGUIElements(JPanel localcomputerPane) {
		
		JLabel sources = new JLabel("Sources");
		
		localcomputerPane.add(sources);
		
		DefaultTableModel sources_model = new DefaultTableModel();
		sources_model.addColumn("Path");
		sources_model.addColumn("Backup Number Locations");

		JTable sources_table = new JTable(sources_model);

		JScrollPane clusterScroll = new JScrollPane(sources_table);
		clusterScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		clusterScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		for (int i = 0; i < LocationSettings.source_locations.size(); i++) {
			sources_model.addRow(new Object[] {
					LocationSettings.source_locations.get(i).path,
					Integer.toString(LocationSettings.source_locations.get(i).backup_locations) });
		}

		localcomputerPane.add(clusterScroll);
		
		return localcomputerPane;
	}
}
