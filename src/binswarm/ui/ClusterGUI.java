package binswarm.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import binswarm.Networking;

public class ClusterGUI {
	public ClusterGUI() {
		//constructor
	}
	
	public static void updateTable() {
		GUI.clusterPane.removeAll(); // remove all old items

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("UUID");
		model.addColumn("IP Address");
		model.addColumn("Last Seen");

		JTable table = new JTable(model);

		JScrollPane clusterScroll = new JScrollPane(table);
		clusterScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		clusterScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		for (int i = 0; i < Networking.computers.size(); i++) {
			model.addRow(new Object[] {
					Networking.computers.get(i).uuid.toString(),
					Networking.computers.get(i).IPAddress,
					Networking.computers.get(i).returnHumanReadableTimestamp() });
		}

		GUI.clusterPane.add(clusterScroll, BorderLayout.CENTER);

		GUI.clusterPane.revalidate();
		GUI.clusterPane.repaint();
	}
}
