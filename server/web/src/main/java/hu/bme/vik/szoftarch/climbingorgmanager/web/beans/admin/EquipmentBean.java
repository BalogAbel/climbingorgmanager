package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchEquipmentException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class EquipmentBean implements Serializable {

	@Inject
	private EquipmentManager equipmentManager;

	@Getter
	@Setter
	private Equipment equipment;

	@Getter
	@Setter
	private List<EquipmentType> equipmentTypes;

	@PostConstruct
	public void init() {
		String idStr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("id");
		equipmentTypes = equipmentManager.getEquipmentTypes();
		if (idStr == null) {
			equipment = new Equipment();
			return;
		}
		try {
			Long id = Long.valueOf(idStr);
			equipment = equipmentManager.getEquipmentById(id);
		} catch (NoSuchEquipmentException | NumberFormatException e) {
			System.out.println("No equipment found with the given id - " + idStr);
		}
	}

	public String save() {
		equipmentManager.editEquipment(equipment);
		return "equipment.xhtml?id=" + equipment.getId();
	}

	public String create() {
		equipmentManager.addEquipment(equipment);
		return "equipment.xhtml?id=" + equipment.getId();
	}


}
