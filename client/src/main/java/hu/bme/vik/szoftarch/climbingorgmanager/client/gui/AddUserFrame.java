package hu.bme.vik.szoftarch.climbingorgmanager.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.UserData;

/**
 * Created by Dani on 2014.11.20..
 */
public class AddUserFrame extends JFrame {

	public AddUserFrame(MainFrame parent) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		final Container contentPane = getContentPane();

		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBorder(new TitledBorder("Create new user"));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 0, 5);
		constraints.anchor = GridBagConstraints.WEST;

		JLabel usernameLabel = new JLabel("Username: ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		inputPanel.add(usernameLabel, constraints);

		final JTextField usernameField = new JTextField("hululu", 20);
		constraints.gridx = 1;
		inputPanel.add(usernameField, constraints);

		JLabel passwordLabel = new JLabel("Password: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(passwordLabel, constraints);

		final JPasswordField passwordField = new JPasswordField("hululu", 20);
		constraints.gridx = 1;
		inputPanel.add(passwordField, constraints);

		JLabel passwordLabel2 = new JLabel("Retype password: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(passwordLabel2, constraints);

		final JPasswordField passwordField2 = new JPasswordField("hululu", 20);
		constraints.gridx = 1;
		inputPanel.add(passwordField2, constraints);

		JLabel firstNameLabel = new JLabel("First name: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(firstNameLabel, constraints);

		final JTextField firstNameField = new JTextField("Hululu", 20);
		constraints.gridx = 1;
		inputPanel.add(firstNameField, constraints);

		JLabel lastNameLabel = new JLabel("Last name: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(lastNameLabel, constraints);

		final JTextField lastNameField = new JTextField("Kiss", 20);
		constraints.gridx = 1;
		inputPanel.add(lastNameField, constraints);

		JLabel addressLabel = new JLabel("Address: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(addressLabel, constraints);

		final JTextField addressField = new JTextField("Hululu u. 15.", 20);
		constraints.gridx = 1;
		inputPanel.add(addressField, constraints);

		JLabel emailLabel = new JLabel("Email: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(emailLabel, constraints);

		final JTextField emailField = new JTextField("hu@lulu.com", 20);
		constraints.gridx = 1;
		inputPanel.add(emailField, constraints);

		JLabel adminLabel = new JLabel("Admin: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(adminLabel, constraints);

		final JCheckBox adminCheckBox = new JCheckBox();
		constraints.gridx = 1;
		inputPanel.add(adminCheckBox, constraints);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("Create");
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);
		buttonPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String password = new String(passwordField.getPassword());
				String password2 = new String(passwordField2.getPassword());
				if (password.equals(password2)) {
					UserData userData = new UserData();
					userData.setFirstName(firstNameField.getText());
					userData.setLastName(lastNameField.getText());
					userData.setAddress(addressField.getText());
					User user = new User();
					user.setUserName(usernameField.getText());
					user.setUserData(userData);
					user.setPassword(password);
					user.setEmail(emailField.getText());
					user.setRegisteredOn(new Date());
					user.setLastLoginOn(new Date());
					user.setAdmin(adminCheckBox.isSelected());

					Controller controller = Controller.getInstance();
					controller.addNewUser(user, AddUserFrame.this);
				} else {
					JOptionPane.showMessageDialog(AddUserFrame.this, "Passwords do not match!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pack();
		setLocationRelativeTo(parent);
	}
}
