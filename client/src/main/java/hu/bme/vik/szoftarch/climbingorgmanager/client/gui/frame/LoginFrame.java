package hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;

public class LoginFrame extends JFrame {

	private JTextField usernameField;
	private JPasswordField passwordField;

	public LoginFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 0, 5);

		JLabel usernameLabel = new JLabel("Username: ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		inputPanel.add(usernameLabel, constraints);

		usernameField = new JTextField("admin", 20);
		constraints.gridx = 1;
		inputPanel.add(usernameField, constraints);

		JLabel passwordLabel = new JLabel("Password: ");
		constraints.gridx = 0;
		constraints.gridy = 1;
		inputPanel.add(passwordLabel, constraints);

		passwordField = new JPasswordField("password", 20);
		constraints.gridx = 1;
		inputPanel.add(passwordField, constraints);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("Sign in");
		buttonPanel.add(okButton);

		SignInActionListener listener = new SignInActionListener();
		okButton.addActionListener(listener);
		usernameField.addActionListener(listener);
		passwordField.addActionListener(listener);

		contentPane.add(inputPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

	public void onLoginFailed(String error) {
		usernameField.setEnabled(true);
		passwordField.setEnabled(true);
		JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private class SignInActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			usernameField.setEnabled(false);
			passwordField.setEnabled(false);

			Controller controller = Controller.getInstance();
			controller.login(LoginFrame.this, usernameField.getText(), new String(passwordField.getPassword()));
		}
	}
}
