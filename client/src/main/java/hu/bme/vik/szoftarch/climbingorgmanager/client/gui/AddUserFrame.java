package hu.bme.vik.szoftarch.climbingorgmanager.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

/**
 * Created by Dani on 2014.11.20..
 */
public class AddUserFrame extends JFrame {

	public AddUserFrame(MainFrame parent) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBorder(new TitledBorder("Create new user"));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 0, 5);
		constraints.anchor = GridBagConstraints.WEST;

		JLabel usernameLabel = new JLabel("Username: ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		inputPanel.add(usernameLabel, constraints);

		final JTextField usernameField = new JTextField(20);
		constraints.gridx = 1;
		inputPanel.add(usernameField, constraints);

		JLabel passwordLabel = new JLabel("Password: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(passwordLabel, constraints);

		final JPasswordField passwordField = new JPasswordField(20);
		constraints.gridx = 1;
		inputPanel.add(passwordField, constraints);

		JLabel passwordLabel2 = new JLabel("Retype password: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(passwordLabel2, constraints);

		final JPasswordField passwordField2 = new JPasswordField(20);
		constraints.gridx = 1;
		inputPanel.add(passwordField2, constraints);

		JLabel firstNameLabel = new JLabel("First name: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(firstNameLabel, constraints);

		final JTextField firstNameField = new JTextField(20);
		constraints.gridx = 1;
		inputPanel.add(firstNameField, constraints);

		JLabel lastNameLabel = new JLabel("Last name: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(lastNameLabel, constraints);

		final JTextField lastNameField = new JTextField(20);
		constraints.gridx = 1;
		inputPanel.add(lastNameField, constraints);

		JLabel addressLabel = new JLabel("Address: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(addressLabel, constraints);

		final JTextField addressField = new JTextField(20);
		constraints.gridx = 1;
		inputPanel.add(addressField, constraints);

		JLabel emailLabel = new JLabel("Email: ");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(emailLabel, constraints);

		final JTextField emailField = new JTextField(20);
		constraints.gridx = 1;
		inputPanel.add(emailField, constraints);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("Create");
		buttonPanel.add(okButton);

		contentPane.add(inputPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(parent);
	}
}
