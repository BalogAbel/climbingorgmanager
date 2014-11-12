package hu.bme.vik.szoftarch.climbingorgamanager.backend.managers;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchEquipmentException;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import lombok.Data;

@Local
@Stateless
public class EquipmentManager {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	@Data
	public static class EquipmentFilter {

		private String name;
		private Long equipmentTypeId;
		private Boolean rented;
		private String accessionNumber;
		private String description;

		public EquipmentFilter(String name, Long equipmentTypeId, Boolean rented, String accessionNumber,
				String description) {
			this.name = name;
			this.equipmentTypeId = equipmentTypeId;
			this.rented = rented;
			this.accessionNumber = accessionNumber;
			this.description = description;
		}
	}

	public List<Equipment> getEquipments(EquipmentFilter filter) {
		if (filter == null) {
			return entityManager.createNamedQuery(Equipment.GET_ALL, Equipment.class).getResultList();
		} else {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Equipment> criteriaQuery = builder.createQuery(Equipment.class);
			Root<Equipment> equipment = criteriaQuery.from(Equipment.class);
			criteriaQuery.select(equipment);

			if (filter.getRented() != null) {
				if (filter.getRented()) {
					criteriaQuery.where(builder.isNotNull(equipment.get("actualRental")));
				} else {
					criteriaQuery.where(builder.isNull(equipment.get("actualRental")));
				}
			}

			if (filter.getAccessionNumber() != null) {
				criteriaQuery.where(builder
						.like(equipment.<String>get("accessionNumber"), "%" + filter.getAccessionNumber() + "%"));
			}

			if (filter.getDescription() != null) {
				criteriaQuery.where(builder
						.like(equipment.<String>get("description"), "%" + filter.getDescription() + "%"));
			}

			if (filter.getName() != null) {
				criteriaQuery.where(builder
						.like(equipment.<String>get("name"), "%" + filter.getName() + "%"));
			}

			if (filter.getEquipmentTypeId() != null) {
				criteriaQuery.where(builder.equal(equipment.get("equipmentType"), filter.getEquipmentTypeId()));
			}
			TypedQuery<Equipment> query = entityManager.createQuery(criteriaQuery);
			return query.getResultList();
		}
	}

	public Equipment getEquipmentById(long id) throws NoSuchEquipmentException {
		TypedQuery<Equipment> query = entityManager.createNamedQuery(Equipment.GET_BY_ID, Equipment.class);
		query.setParameter("id", id);
		List<Equipment> equipments = query.getResultList();
		if (equipments.size() != 1) {
			throw new NoSuchEquipmentException();
		}
		return equipments.get(0);
	}

	public void addEquipment(Equipment equipment) {
		entityManager.merge(equipment);
	}

	public void removeEquipment(Equipment equipment) {
		Equipment attachedEquipment = entityManager.merge(equipment);
		entityManager.remove(attachedEquipment);
	}
}
