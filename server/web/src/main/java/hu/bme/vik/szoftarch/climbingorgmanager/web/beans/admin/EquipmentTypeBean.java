package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchEquipmentTypeException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
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

@Named
@RequestScoped
public class EquipmentTypeBean implements Serializable {
	@Inject
	private EquipmentManager equipmentManager;

	@Getter
	@Setter
	private EquipmentType equipmentType;

	@PostConstruct
	public void init() {
		String idStr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("id");
		if (idStr == null) {
			equipmentType = new EquipmentType();
			return;
		}
		try {
			Long id = Long.valueOf(idStr);
			equipmentType = equipmentManager.getEquipmentTypeById(id);
		} catch (NoSuchEquipmentTypeException | NumberFormatException e) {
			System.out.println("No equipment type found with the given id - " + idStr);
		}
	}

	public String save() {
		equipmentManager.editEquipmentType(equipmentType);
		return "equipmentType.xhtml?id=" + equipmentType.getId();
	}

	public String create() {
		equipmentManager.addEquipmentType(equipmentType);
		return "equipmentType.xhtml?id=" + equipmentType.getId();
	}

}
