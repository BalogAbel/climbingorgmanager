package hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.28..
 */
public interface SelectedUserChangeListener {

	public void onSelectedUserChanged(User selectedUser);
}
