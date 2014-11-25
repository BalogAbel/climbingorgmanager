package hu.bme.vik.szoftarch.climbingorgmanager.web.beans;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.RentalManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class MyRentalsBean {
	@Inject
	private RentalManager rentalManager;

	@Setter
	@ManagedProperty(value = "#{authBean}")
	private AuthBean authBean;

	@Getter
	private List<Rental> activeRentals;

	@Getter
	private List<Rental> passiveRentals;

	@PostConstruct
	public void init() {
		List<Rental> rentals = rentalManager.getRentalsForUser(authBean.getUser());
		activeRentals = new ArrayList<>();
		passiveRentals = new ArrayList<>();
		for (Rental rental : rentals) {
			if (rental.getReturnedOn() == null) activeRentals.add(rental);
			else passiveRentals.add(rental);
		}
	}
}
