package hu.bme.vik.szoftarch.climbingorgmanager.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.PassChooserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.28..
 */
public class UserPassesListPanel extends JPanel {

	private List<Pass> passes;
	private Controller controller;

	public UserPassesListPanel() {
		setLayout(new BorderLayout());
		controller = Controller.getInstance();
		passes = Collections.emptyList();
		initGui();
	}

	private void initGui() {

		if (passes.isEmpty()) {
			add(new JLabel("No passes found"), BorderLayout.CENTER);
		} else {
			JPanel listPanel = new JPanel();
			JScrollPane scrollPane = new JScrollPane(listPanel);
			add(scrollPane, BorderLayout.CENTER);
			scrollPane.setMaximumSize(new Dimension(250, 200));
			listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

			for (Pass pass : passes) {
				if (pass.isUsable()) {
					listPanel.add(new UserPassPanel(pass));
				}
			}
		}

		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);

		JButton buyPassButton = new JButton("Buy pass");
		buttonPanel.add(buyPassButton);
		buyPassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PassChooserFrame frame = new PassChooserFrame();
				frame.setVisible(true);
			}
		});

		JButton buyTicketButton = new JButton("Buy ticket");
		buttonPanel.add(buyTicketButton);
		buyTicketButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.enterWithTicket();
			}
		});
	}

	public void setPasses(List<Pass> passes) {
		this.passes = passes;
		removeAll();
		initGui();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getParent().revalidate();
				getParent().repaint();
			}
		});
	}

	private class UserPassPanel extends JPanel {

		private UserPassPanel(final Pass pass) {
			setBorder(BorderFactory.createLineBorder(Color.black));
			setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 0, 5);
			constraints.anchor = GridBagConstraints.WEST;

			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 2;
			add(new JLabel("Times left: " + pass.getTimeLeft()), constraints);
			constraints.gridy++;
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			add(new JLabel("Valid: " + df.format(pass.getValidUntil())), constraints);

			if (pass.isUsable()) {
				JButton useButton = new JButton("Use");
				constraints.gridwidth = 1;
				constraints.gridy++;
				add(useButton, constraints);
				useButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						User user = controller.getSelectedUser();
						controller.enterWithPass(user.getId(), pass.getId());
					}
				});

				JButton useForSomeoneButton = new JButton("Use for someone");
				constraints.gridx++;
				constraints.anchor = GridBagConstraints.EAST;
				add(useForSomeoneButton, constraints);
			}
		}
	}
}
