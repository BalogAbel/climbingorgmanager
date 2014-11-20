package hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.UserData;

/**
 * Created by Dani on 2014.11.20..
 */
public class UserTableModel extends AbstractTableModel {

	private String[] columnNames = new String[]{"Username", "Name", "Email", "Address"};
	private List<User> users;

	public UserTableModel() {
		this.users = new LinkedList<User>();
		setupDemoData();
	}

	private void setupDemoData() {
		users.add(new User(1l, "username1", "pass1", "email1@email.com", new Date(), new Date(),
				new UserData(1l, "First1", "Last1", "Kis utca 1."), false, false));
		users.add(new User(2l, "username2", "pass2", "email2@email.com", new Date(), new Date(),
				new UserData(1l, "First2", "Last2", "Kis utca 2."), false, false));
		users.add(new User(3l, "username3", "pass3", "email3@email.com", new Date(), new Date(),
				new UserData(1l, "First3", "Last3", "Kis utca 3."), false, false));
		users.add(new User(4l, "username4", "pass4", "email4@email.com", new Date(), new Date(),
				new UserData(1l, "First4", "Last4", "Kis utca 4."), false, false));
		users.add(new User(5l, "username5", "pass5", "email5@email.com", new Date(), new Date(),
				new UserData(1l, "First5", "Last5", "Kis utca 5."), false, false));
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
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		User user = users.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return user.getUserName();
			case 1:
				return user.getUserData().getFirstName() + user.getUserData().getLastName();
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
		System.out.println("fireTableDataChanged");
	}
}
