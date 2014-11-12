package hu.bme.vik.szoftarch.climbingorgamanager.backend.managers;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Local
@Stateless
public class RentalManager {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public void rentEquipment(User user, Equipment equipment) {
		User managedUser = entityManager.merge(user);
		Equipment managedEquipment = entityManager.merge(equipment);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		Date rentedUntil = calendar.getTime();

		Rental rental = new Rental();
		rental.setUser(managedUser);
		rental.setRentedOn(new Date());
		rental.setEquipment(managedEquipment);
		rental.setRentedUntil(rentedUntil);

		entityManager.merge(rental);

		managedEquipment.setActualRental(rental);
		entityManager.merge(managedEquipment);
	}

	public void returnEquipment(Equipment equipment) {
		Equipment mergedEntity = entityManager.merge(equipment);
		Rental rental = mergedEntity.getActualRental();
		mergedEntity.setActualRental(null);
		rental.setRentedOn(new Date());

		entityManager.merge(mergedEntity);
		entityManager.merge(rental);
	}
}
