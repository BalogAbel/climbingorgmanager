package hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame;

import org.jdesktop.swingx.JXTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.UserTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.UserData;

/**
 * Created by Dani on 2014.11.29..
 */
public class UserChooserFrame extends JFrame {

	private List<User> selectedUsers;
	private final JPanel selectedUsersPanel;

	public UserChooserFrame(final long passId) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		final Container contentPane = getContentPane();

		selectedUsers = new LinkedList<>();
		final Controller controller = Controller.getInstance();
		final List<User> users = controller.getUsers();

		final JXTable userTable = new JXTable();
		JScrollPane scrollPane = new JScrollPane(userTable);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		UserTableModel userTableModel = new UserTableModel();
		userTable.setModel(userTableModel);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userTableModel.setUsers(users);

		userTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JXTable table = (JXTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2) {
					User user = users.get(row);
					addSelectedUser(user);
				}
			}
		});

		JPanel rightPanel = new JPanel(new BorderLayout());
		contentPane.add(rightPanel, BorderLayout.EAST);

		JPanel guestPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.add(guestPanel, BorderLayout.NORTH);

		JButton guestButton = new JButton("Add guest");
		guestPanel.add(guestButton);
		guestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User guest = new User();
				guest.setId(-1l);
				guest.setRegisteredOn(new Date());
				guest.setEmail("");
				guest.setPassword("");
				guest.setUserName("");
				guest.setAdmin(false);
				UserData userData = new UserData();
				userData.setAddress("");
				userData.setFirstName("GUEST");
				userData.setLastName("");
				guest.setUserData(userData);
				addSelectedUser(guest);
			}
		});

		selectedUsersPanel = new JPanel();
		rightPanel.add(selectedUsersPanel, BorderLayout.CENTER);
		selectedUsersPanel.setPreferredSize(new Dimension(200, 200));
		selectedUsersPanel.setBorder(new TitledBorder("Selected users"));
		selectedUsersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedUsers.isEmpty()) {
					dispose();
				} else {
					controller.enterWithPass(UserChooserFrame.this, selectedUsers, passId);
				}
			}
		});

		pack();
		setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
	}

	private void addSelectedUser(User user) {
		selectedUsers.add(user);
		selectedUsersPanel.add(new ChosenUserPanel(user));

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				selectedUsersPanel.revalidate();
				selectedUsersPanel.repaint();
			}
		});
	}

	private class ChosenUserPanel extends JPanel {

		private ChosenUserPanel(final User user) {
			setLayout(new FlowLayout(FlowLayout.LEFT));
			setAlignmentX(Component.LEFT_ALIGNMENT);

			JButton removeButton = new JButton("Remove");
			add(removeButton);
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedUsers.remove(user);
					selectedUsersPanel.remove(ChosenUserPanel.this);

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							selectedUsersPanel.revalidate();
							selectedUsersPanel.repaint();
						}
					});
				}
			});

			UserData userData = user.getUserData();
			JLabel nameLabel = new JLabel(userData.getFirstName() + " " + userData.getLastName());
			add(nameLabel);
		}
	}
}
