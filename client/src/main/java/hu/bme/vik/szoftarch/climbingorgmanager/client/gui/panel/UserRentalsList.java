package hu.bme.vik.szoftarch.climbingorgmanager.client.gui.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;

public class UserRentalsList extends JPanel {

	private List<Rental> rentals;

	public UserRentalsList() {
		this.rentals = new LinkedList<>();

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	private void update() {
		removeAll();
		if (rentals.isEmpty()) {
			add(new JLabel("No active rentals."));
		} else {
			for (Rental rental : rentals) {
				add(new UserRentalPanel(rental));
			}
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getParent().revalidate();
				getParent().repaint();
			}
		});
	}

	public void setRentals(List<Rental> rentals) {
		this.rentals = rentals;
		update();
	}

	private class UserRentalPanel extends JPanel {

		private UserRentalPanel(final Rental rental) {
			setLayout(new FlowLayout(FlowLayout.LEFT));

			JLabel label = new JLabel(rental.getEquipment().getName());
			add(label);

			JButton returnButton = new JButton("Return");
			add(returnButton);
			returnButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Controller controller = Controller.getInstance();
					controller.returnEquipment(rental.getId());
				}
			});
		}
	}
}
