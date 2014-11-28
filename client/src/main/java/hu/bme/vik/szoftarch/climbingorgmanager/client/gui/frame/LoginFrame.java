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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgmanager.client.util.GsonMessageBodyHandler;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Token;

public class LoginFrame extends JFrame {

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

		final JTextField usernameField = new JTextField("teszt", 20);
		constraints.gridx = 1;
		inputPanel.add(usernameField, constraints);

		JLabel passwordLabel = new JLabel("Password: ");
		constraints.gridx = 0;
		constraints.gridy = 1;
		inputPanel.add(passwordLabel, constraints);

		final JPasswordField passwordField = new JPasswordField("password", 20);
		constraints.gridx = 1;
		inputPanel.add(passwordField, constraints);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("Sign in");
		buttonPanel.add(okButton);

		SignInActionListener listener = new SignInActionListener(usernameField, passwordField);
		okButton.addActionListener(listener);
		usernameField.addActionListener(listener);
		passwordField.addActionListener(listener);

		contentPane.add(inputPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

	private class SignInActionListener implements ActionListener {

		private JTextField usernameField;
		private JPasswordField passwordField;

		private SignInActionListener(JTextField usernameField, JPasswordField passwordField) {
			this.usernameField = usernameField;
			this.passwordField = passwordField;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			usernameField.setEnabled(false);
			passwordField.setEnabled(false);

			Form form = new Form();
			form.param("username", usernameField.getText());
			form.param("password", new String(passwordField.getPassword()));
			Client client = ClientBuilder.newClient().register(GsonMessageBodyHandler.class);
			client.target("http://climbingorgmanager-asztalosdani.rhcloud.com/rest-1.0-SNAPSHOT/rest")
					.path("users/login").request(MediaType.APPLICATION_JSON_TYPE)
					.async().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
					new InvocationCallback<Response>() {
						@Override
						public void completed(Response response) {
							usernameField.setEnabled(true);
							passwordField.setEnabled(true);

							System.out.println(response);
							Token token = response.readEntity(Token.class);
							System.out.println("Token: " + token.getToken());
							System.out.println("User: " + token.getUser().getUserName());
							MainFrame frame = new MainFrame();
							frame.setVisible(true);
							LoginFrame.this.setVisible(false);
						}

						@Override
						public void failed(Throwable throwable) {
							System.out.println("Invocation failed.");
							throwable.printStackTrace();
							usernameField.setEnabled(true);
							passwordField.setEnabled(true);
						}
					});
		}
	}
}
