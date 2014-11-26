package hu.bme.vik.szoftarch.climbingorgmanager.web.beans;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
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
public class EquipmentsBean implements Serializable {

	@Inject
	private EquipmentManager equipmentManager;

	@Getter
	@Setter
	private List<Equipment> equipments;

	@Getter
	@Setter
	private Equipment selectedEquipment;

	@PostConstruct
	public void init() {
		equipments = equipmentManager.getEquipments(null);
	}
}
