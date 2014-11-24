package hu.bme.vik.szoftarch.climbingorgamanager.backend.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EquipmentAlreadyRentedException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchRentalException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UserNotRecognizedMemberException;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Local
@Stateless
public class RentalManager {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public List<Rental> getRentals() {
		return entityManager.createNamedQuery(Rental.GET_ALL, Rental.class).getResultList();
	}

	public Rental getRental(long rentalId) throws NoSuchRentalException {
		TypedQuery<Rental> query = entityManager.createNamedQuery(Rental.GET_BY_ID, Rental.class);
		query.setParameter("id", rentalId);
		List<Rental> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			throw new NoSuchRentalException();
		}
		return resultList.get(0);
	}

	public void rentEquipment(User user, Equipment equipment)
			throws EquipmentAlreadyRentedException, UserNotRecognizedMemberException {
		User managedUser = entityManager.merge(user);
		Equipment managedEquipment = entityManager.merge(equipment);
		if (managedEquipment.getActualRental() != null) {
			throw new EquipmentAlreadyRentedException();
		}
		if (!managedUser.isRecognizedMember()) {
			throw new UserNotRecognizedMemberException();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		Date rentedUntil = calendar.getTime();

		Rental rental = new Rental();
		rental.setUser(managedUser);
		rental.setRentedOn(new Date());
		rental.setEquipment(managedEquipment);
		rental.setRentedUntil(rentedUntil);
		System.out.println("***** 1");
		entityManager.persist(rental);
		System.out.println("***** 2");

		managedEquipment.setActualRental(rental);
		entityManager.merge(managedEquipment);
		System.out.println("***** 3");
	}

	public void returnEquipment(Rental rental) {
		Rental managedRental = entityManager.merge(rental);
		Equipment equipment = managedRental.getEquipment();
		equipment.setActualRental(null);
		rental.setReturnedOn(new Date());

		entityManager.merge(equipment);
		entityManager.merge(rental);
	}

	public List<Rental> getRentalsForUser(User user) {
		User managedUser = entityManager.merge(user);
		TypedQuery<Rental> query = entityManager.createNamedQuery(Rental.GET_BY_USER, Rental.class);
		query.setParameter("user", managedUser);
		return query.getResultList();
	}

	public List<Rental> getActiveRentalsForUser(User user) {
		User managedUser = entityManager.merge(user);
		TypedQuery<Rental> query = entityManager.createNamedQuery(Rental.GET_ACTIVE_BY_USER, Rental.class);
		query.setParameter("user", managedUser);
		return query.getResultList();
	}
}
