package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EntryManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.RentalManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class MembersBean implements Serializable {

	@Getter
	@Setter
	private List<User> members;

	@Getter
	@Setter
	private List<User> filteredMembers;

	@Getter
	@Setter
	private User selectedMember;

	@Inject
	private UserManager userManager;

	@Inject
	private RentalManager rentalManager;

	@Inject
	private EntryManager entryManager;


	@PostConstruct
	public void init() {
		members = userManager.getUsers();
	}

	public void changeAdminStatus() {
		selectedMember.setAdmin(!selectedMember.isAdmin());
		userManager.editUser(selectedMember);
		init();
	}

	public void remove() {
		userManager.removeUser(selectedMember);
		init();
	}

	public void enterGuest() {
		entryManager.guestEntry();
		FacesContext.getCurrentInstance().addMessage("guestEntry", new FacesMessage("Successful", "Guest entry was successful"));
	}

}

