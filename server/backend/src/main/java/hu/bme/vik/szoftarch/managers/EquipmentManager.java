package hu.bme.vik.szoftarch.managers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import hu.bme.vik.szoftarch.entities.Equipment;
import hu.bme.vik.szoftarch.entities.EquipmentType;

@Local
@Stateless
public class EquipmentManager {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public List<Equipment> getEquipments() {
		return entityManager.createNamedQuery(Equipment.GET_ALL, Equipment.class).getResultList();
	}

	public void addEquipment(Equipment equipment) {
		entityManager.merge(equipment);
	}
}
