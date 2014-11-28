package hu.bme.vik.szoftarch.climbingorgmanager.client.gui.panel;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners.RentalsForSelectedUserLoadedListener;
import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners.SelectedUserChangeListener;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.EditUserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.PassChooserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.20..
 */
public class UserDetailsPanel extends JPanel implements SelectedUserChangeListener,
		RentalsForSelectedUserLoadedListener {

	private User user;
	private JLabel nameLabel;
	private JLabel usernameLabel;
	private JLabel emailLabel;
	private JLabel addressLabel;
	private UserRentalsList userRentalsList;

	private Controller controller;

	public UserDetailsPanel(final JFrame parent) {
		controller = Controller.getInstance();
		controller.addSelectedUserChangeListener(this);

		setLayout(new BorderLayout());
		setBorder(new TitledBorder("User details"));
		setPreferredSize(new Dimension(200, 100));

		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.CENTER);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));

		nameLabel = new JLabel("Select a user!");
		topPanel.add(nameLabel);
		usernameLabel = new JLabel();
		topPanel.add(usernameLabel);
		emailLabel = new JLabel();
		topPanel.add(emailLabel);
		addressLabel = new JLabel();
		topPanel.add(addressLabel);

		JButton editButton = new JButton("Edit user");
		topPanel.add(editButton);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditUserFrame editUserFrame = new EditUserFrame(parent, user);
				editUserFrame.setVisible(true);
			}
		});

		JPanel lowerPanel = new JPanel(new BorderLayout());
		add(lowerPanel, BorderLayout.SOUTH);

		final JPanel passesPanel = new JPanel();
		lowerPanel.add(passesPanel, BorderLayout.NORTH);
		passesPanel.setBorder(new TitledBorder("Passes"));

		final JXCollapsiblePane passesPane = new JXCollapsiblePane();
		passesPanel.add(passesPane);
		passesPane.setCollapsed(true);

		JXTable passesTable = new JXTable();
		JScrollPane passesScrollPane = new JScrollPane(passesTable);
		passesPane.add(passesScrollPane, BorderLayout.CENTER);
		passesTable.setVisibleRowCount(5);
		passesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		final JButton passesButton = new JButton("Buy ticket/pass");
		passesPane.add(passesButton, BorderLayout.SOUTH);
		passesButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PassChooserFrame frame = new PassChooserFrame();
				frame.setVisible(true);
			}
		});

		final JPanel equipmentsPanel = new JPanel();
		lowerPanel.add(equipmentsPanel, BorderLayout.SOUTH);
		equipmentsPanel.setBorder(new TitledBorder("Equipments"));

		final JXCollapsiblePane equipmentsPane = new JXCollapsiblePane();
		equipmentsPanel.add(equipmentsPane);
		equipmentsPane.setCollapsed(true);

		userRentalsList = new UserRentalsList();
		equipmentsPane.add(userRentalsList, BorderLayout.CENTER);
		controller.addRentalsForSelectedUserLoadedListener(this);

		// Mouse listeners for toggling collapsible panes
		equipmentsPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				int y = me.getPoint().y;
				Insets ins = equipmentsPanel.getInsets();
				boolean inBorder = y < ins.top;
				if (inBorder || equipmentsPane.isCollapsed()) {
					equipmentsPane.setCollapsed(!equipmentsPane.isCollapsed());
					passesPane.setCollapsed(true);
				}
			}
		});
		passesPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				int y = me.getPoint().y;
				Insets ins = passesPanel.getInsets();
				boolean inBorder = y < ins.top;
				if (inBorder || passesPane.isCollapsed()) {
					passesPane.setCollapsed(!passesPane.isCollapsed());
					equipmentsPane.setCollapsed(true);
				}
			}
		});

	}

	private void update() {
		if (user == null) {
			nameLabel.setText("Select a user!");
			usernameLabel.setText("");
			emailLabel.setText("");
			addressLabel.setText("");
		} else {
			nameLabel.setText(user.getUserData().getFirstName() + " " + user.getUserData().getLastName());
			usernameLabel.setText(user.getUserName());
			emailLabel.setText(user.getEmail());
			addressLabel.setText(user.getUserData().getAddress());
		}

	}

	@Override
	public void onSelectedUserChanged(User selectedUser) {
		this.user = selectedUser;
		update();
	}

	@Override
	public void onRentalsLoaded(List<Rental> rentals) {
		userRentalsList.setRentals(rentals);
	}
}
