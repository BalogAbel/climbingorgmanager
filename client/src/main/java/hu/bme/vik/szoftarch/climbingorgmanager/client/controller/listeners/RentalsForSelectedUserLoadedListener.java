package hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners;

import java.util.List;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;

/**
 * Created by Dani on 2014.11.28..
 */
public interface RentalsForSelectedUserLoadedListener {
	public void onRentalsLoaded(List<Rental> rentals);
}
