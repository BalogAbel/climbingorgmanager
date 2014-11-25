package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EquipmentAlreadyRentedException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UserNotRecognizedMemberException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.RentalManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class RentalBean implements Serializable {

	@Inject
	private UserManager userManager;

	@Inject
	private EquipmentManager equipmentManager;

	@Inject
	private RentalManager rentalManager;

	@Getter
	private User user;

	/**
	 * 0 - succes; 1 - already rented; 2 - user is not recognized
	 */
	@Getter
	private Map<Equipment, Integer> result;

	@Getter
	@Setter
	private List<Equipment> filteredEquipments;

	@Getter
	@Setter
	private List<Equipment> selectedEquipments;


	@Getter
	private List<Equipment> equipments;

	@PostConstruct
	public void init() {
		String idStr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("id");
		try {
			Long id = Long.valueOf(idStr);
			user = userManager.getUserById(id);
			equipments = equipmentManager.getEquipments(null);
		} catch (NoSuchUserException | NumberFormatException e) {
			System.out.println("No user found with the given id - " + idStr);
		}
	}

	public void rent() {
		result = new HashMap<Equipment, Integer>();
		for (Equipment equipment : selectedEquipments) {
			try {
				rentalManager.rentEquipment(user, equipment);
				result.put(equipment, 0);
			} catch (EquipmentAlreadyRentedException e) {
				result.put(equipment, 1);
			} catch (UserNotRecognizedMemberException e) {
				result.put(equipment, 2);
			}
		}
	}


}
