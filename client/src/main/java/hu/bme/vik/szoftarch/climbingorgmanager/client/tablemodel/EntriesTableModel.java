package hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.28..
 */
public class EntriesTableModel extends AbstractTableModel {

	private static final String[] columnNames = new String[]{"Date", "User", "Type", "Owner of pass"};
	private List<Entry> entries;

	public EntriesTableModel() {
		this.entries = new LinkedList<>();
	}

	@Override
	public int getRowCount() {
		return entries.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Entry entry = entries.get(rowIndex);
		Pass pass = entry.getPass();
		switch (columnIndex) {
			case 0:
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				return df.format(entry.getEnteredOn());
			case 1:
				User user = entry.getUser();
				if (user == null) {
					return "GUEST";
				} else {
					return user.getUserName();
				}
			case 2:
				if (pass == null) {
					return "TICKET";
				} else {
					return "PASS";
				}
			case 3:
				if (pass == null || pass.getOwner() == null) {
					return "-";
				} else {
					return pass.getOwner().getUserName();
				}
			default:
				return "NOT IMPLEMENTED YET...";
		}
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
		fireTableDataChanged();
	}
}
