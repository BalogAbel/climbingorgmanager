package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.RentalManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ReturnEquipmentBean implements Serializable {
	@Inject
	private UserManager userManager;

	@Inject
	private RentalManager rentalManager;

	@Getter
	private User user;

	@Getter
	private List<Rental> rentals;

	@Getter
	private List<Rental> returnedRentals;

	@Getter
	@Setter
	private List<Rental> selectedRentals;

	@Getter
	@Setter
	private List<Rental> filteredRentals;

	@PostConstruct
	public void init() {
		String idStr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("id");
		try {
			Long id = Long.valueOf(idStr);
			user = userManager.getUserById(id);
			updateRentals();
		} catch (NoSuchUserException | NumberFormatException e) {
			System.out.println("No user found with the given id - " + idStr);
		}
	}


	private void updateRentals() {
		List<Rental> rentalsAll = rentalManager.getRentalsForUser(user);
		rentals = new ArrayList<Rental>();
		returnedRentals = new ArrayList<Rental>();
		for (Rental rental : rentalsAll) {
			if (rental.getReturnedOn() == null) rentals.add(rental);
			else returnedRentals.add(rental);
		}
	}

	public void returnEquipments() {
		for (Rental rental : selectedRentals) {
			rentalManager.returnEquipment(rental);
		}
		updateRentals();
	}
}
