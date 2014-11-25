package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EquipmentTypesBean implements Serializable {
	@Inject
	private EquipmentManager equipmentManager;

	@Getter
	@Setter
	private List<EquipmentType> equipmentTypes;

	@Getter
	@Setter
	private EquipmentType selectedEquipmentType;

	@Getter
	@Setter
	private List<EquipmentType> filteredEquipmentTypes;

	@PostConstruct
	public void init() {
		equipmentTypes = equipmentManager.getEquipmentTypes();
	}
}
