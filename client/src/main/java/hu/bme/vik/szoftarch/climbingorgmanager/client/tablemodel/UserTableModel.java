package hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.20..
 */
public class UserTableModel extends AbstractTableModel {

	private String[] columnNames = new String[]{"Username", "Name", "Email", "Address"};
	private List<User> users;

	public UserTableModel() {
		this.users = new LinkedList<User>();
	}

	@Override
	public int getRowCount() {
		return users.size();
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
		User user = users.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return user.getUserName();
			case 1:
				return user.getUserData().getFirstName() + " " + user.getUserData().getLastName();
			case 2:
				return user.getEmail();
			case 3:
				return user.getUserData().getAddress();
			default:
				return "NOT IMPLEMENTED YET...";
		}
	}

	public User getUser(int rowIndex) {
		return users.get(rowIndex);
	}

	public void setUsers(List<User> users) {
		this.users = users;
		fireTableDataChanged();
	}
}
