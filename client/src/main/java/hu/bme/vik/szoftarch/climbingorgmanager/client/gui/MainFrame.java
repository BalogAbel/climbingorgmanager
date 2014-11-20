package hu.bme.vik.szoftarch.climbingorgmanager.client.gui;

import org.jdesktop.swingx.JXTable;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.UserTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

public class MainFrame extends JFrame {

	private UserDetailsPanel detailsPanel;
	private UserTableModel userTableModel;

	public MainFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();

		JTabbedPane tabbedPane = new JTabbedPane();
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel usersPanel = new JPanel(new BorderLayout());
		final JXTable userTable = new JXTable();
		JScrollPane scrollPane = new JScrollPane(userTable);
		usersPanel.add(scrollPane, BorderLayout.CENTER);

		userTableModel = new UserTableModel();
		userTable.setModel(userTableModel);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userTable.setFillsViewportHeight(true);

		Controller controller = Controller.getInstance();
		controller.loadUsers(new Controller.ServiceCallback<List<User>>() {
			@Override
			public void onCompleted(List<User> users) {
				userTableModel.setUsers(users);
			}

			@Override
			public void onFailed(String errorMessage) {
				JOptionPane.showMessageDialog(MainFrame.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		ListSelectionModel selectionModel = userTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				int selectedIndex = userTable.getSelectedRow();
				User selectedUser = userTableModel.getUser(selectedIndex);
				detailsPanel.setUser(selectedUser);
			}
		});

		JPanel userControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		usersPanel.add(userControlsPanel, BorderLayout.SOUTH);

		JButton newUserButton = new JButton("Add new user");
		userControlsPanel.add(newUserButton);
		newUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddUserFrame frame = new AddUserFrame(MainFrame.this);
				frame.setVisible(true);
			}
		});
		tabbedPane.addTab("Users", usersPanel);

		JPanel equipmentsPanel = new JPanel();
		tabbedPane.addTab("Equipments", equipmentsPanel);

		JPanel entriesPanel = new JPanel();
		tabbedPane.addTab("Entries", entriesPanel);

		detailsPanel = new UserDetailsPanel();
		contentPane.add(detailsPanel, BorderLayout.EAST);

		JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel statusBarLabel = new JLabel("Logged in as: " + "SUPERADMIN");
		statusBar.add(statusBarLabel);
		JButton logoutButton = new JButton("Logout");
		statusBar.add(logoutButton);

		contentPane.add(statusBar, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
//		LoginFrame loginFrame = new LoginFrame();
//		loginFrame.setVisible(true);

		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);

	}
}
