package hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners;

import java.util.List;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.28..
 */
public interface ChangeListener {

	public void onRentalsLoaded(List<Rental> rentals);

	public void onSelectedUserChanged(User selectedUser);

	public void onPassesLoaded(List<Pass> passes);
}
