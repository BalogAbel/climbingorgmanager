package hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame;

import org.jdesktop.swingx.JXTable;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.panel.EntriesControlPanel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.panel.UserDetailsPanel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EntriesTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EquipmentTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.UserTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

public class MainFrame extends JFrame {

	private UserTableModel userTableModel;
	private Controller controller;

	public MainFrame(User loggedInUser) {
		controller = Controller.getInstance();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();

		final JTabbedPane tabbedPane = new JTabbedPane();
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel usersPanel = createUsersPanel();
		tabbedPane.addTab("Users", usersPanel);

		JPanel equipmentsPanel = createEquipmentsPanel();
		tabbedPane.addTab("Equipments", equipmentsPanel);

		JPanel entriesPanel = createEntriesPanel();
		tabbedPane.addTab("Entries", entriesPanel);

		JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel statusBarLabel = new JLabel("Logged in as: " + loggedInUser.getUserName());
		statusBar.add(statusBarLabel);
		JButton logoutButton = new JButton("Logout");
		statusBar.add(logoutButton);
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.logout();
				LoginFrame loginFrame = new LoginFrame();
				loginFrame.setVisible(true);

				dispose();
			}
		});

		contentPane.add(statusBar, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);

		controller.bind(this, userTableModel);
		controller.loadUsers();
		controller.loadEquipments();
	}

	private JPanel createUsersPanel() {
		JPanel usersPanel = new JPanel(new BorderLayout());

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		usersPanel.add(buttonPanel, BorderLayout.NORTH);

		JButton refreshButton = new JButton("Refresh");
		buttonPanel.add(refreshButton);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.loadUsers();
			}
		});

		UserDetailsPanel detailsPanel = new UserDetailsPanel(this);
		usersPanel.add(detailsPanel, BorderLayout.EAST);

		final JXTable userTable = new JXTable();
		JScrollPane scrollPane = new JScrollPane(userTable);
		usersPanel.add(scrollPane, BorderLayout.CENTER);

		userTableModel = new UserTableModel();
		userTable.setModel(userTableModel);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ListSelectionModel selectionModel = userTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				int selectedIndex = userTable.getSelectedRow();
				if (selectedIndex > -1) {
					User selectedUser = userTableModel.getUser(selectedIndex);
					controller.setSelectedUser(selectedUser);
				} else {
					controller.setSelectedUser(null);
				}
			}
		});

		JPanel userControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		usersPanel.add(userControlsPanel, BorderLayout.SOUTH);

		JButton newUserButton = new JButton("Add new user");
		userControlsPanel.add(newUserButton);
		newUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditUserFrame frame = new EditUserFrame(MainFrame.this);
				frame.setVisible(true);
			}
		});

		JButton guestTicketButton = new JButton("Enter as GUEST");
		userControlsPanel.add(guestTicketButton);
		guestTicketButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.enterWithTicket(-1l);

			}
		});

		return usersPanel;
	}

	private JPanel createEquipmentsPanel() {
		JPanel equipmentsPanel = new JPanel(new BorderLayout());

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		equipmentsPanel.add(buttonPanel, BorderLayout.NORTH);

		JButton refreshButton = new JButton("Refresh");
		buttonPanel.add(refreshButton);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.loadEquipments();
			}
		});

		UserDetailsPanel detailsPanel = new UserDetailsPanel(this);
		equipmentsPanel.add(detailsPanel, BorderLayout.EAST);

		JXTable equipmentTable = new JXTable();
		JScrollPane scrollPane = new JScrollPane(equipmentTable);
		equipmentsPanel.add(scrollPane, BorderLayout.CENTER);

		EquipmentTableModel equipmentTableModel = new EquipmentTableModel();
		equipmentTable.setModel(equipmentTableModel);
		equipmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		controller.setEquipmentTableModel(equipmentTableModel);

		equipmentTable.getColumn(3).setCellRenderer(new EquipmentTableModel.EquipmentButtonRenderer());
		equipmentTable.getColumn(3).setCellEditor(new EquipmentTableModel.EquipmentButtonEditor());

		JPanel equipmentControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		equipmentsPanel.add(equipmentControlsPanel, BorderLayout.SOUTH);

		JButton newEquipmentButton = new JButton("Add new equipment");
		equipmentControlsPanel.add(newEquipmentButton);
		newEquipmentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditEquipmentFrame frame = new EditEquipmentFrame(MainFrame.this);
				frame.setVisible(true);
			}
		});

		return equipmentsPanel;
	}

	private JPanel createEntriesPanel() {
		JPanel entriesPanel = new JPanel(new BorderLayout());

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		entriesPanel.add(buttonPanel, BorderLayout.NORTH);

		JButton refreshButton = new JButton("Refresh");
		buttonPanel.add(refreshButton);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.loadEntries();
			}
		});

		JXTable entriesTable = new JXTable();
		JScrollPane scrollPane = new JScrollPane(entriesTable);
		entriesPanel.add(scrollPane, BorderLayout.CENTER);

		EntriesTableModel entriesTableModel = new EntriesTableModel();
		entriesTable.setModel(entriesTableModel);
		entriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		controller.setEntriesTableModel(entriesTableModel);

		JPanel entriesControlPanel = new EntriesControlPanel(entriesTable);
		entriesPanel.add(entriesControlPanel, BorderLayout.EAST);

		return entriesPanel;
	}
}
