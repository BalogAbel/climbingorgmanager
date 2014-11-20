package hu.bme.vik.szoftarch.climbingorgmanager.client.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.20..
 */
public class UserDetailsPanel extends JPanel {

	private User user;
	private JLabel nameLabel;

	public UserDetailsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new TitledBorder("User details"));
		nameLabel = new JLabel("Kiss Pista");
		add(nameLabel);
	}

	public void setUser(User user) {
		this.user = user;
		update();
	}

	private void update() {
		nameLabel.setText(user.getUserName());
	}
}
