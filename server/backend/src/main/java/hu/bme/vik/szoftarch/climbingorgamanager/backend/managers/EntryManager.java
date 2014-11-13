package hu.bme.vik.szoftarch.climbingorgamanager.backend.managers;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoMoreTimesOnPassException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.PassExpiredException;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Local
@Stateless
public class EntryManager {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public List<Entry> getEntries() {
		return entityManager.createNamedQuery(Entry.GET_ALL, Entry.class).getResultList();
	}

	public void enterWithTicket(User user) {
		User managedUser = entityManager.merge(user);

		Pass pass = new Pass();
		Date now = new Date();
		pass.setBoughtOn(now);
		pass.setOwner(managedUser);
		pass.setTimeLeft(0);
		pass.setValidUntil(now);
		Pass managedPass = entityManager.merge(pass);

		Entry entry = new Entry();
		entry.setEnteredOn(now);
		entry.setPass(managedPass);
		entry.setUser(managedUser);

		entityManager.merge(entry);
	}

	public void enterWithPass(User user, Pass pass) throws PassExpiredException, NoMoreTimesOnPassException {
		Date now = new Date();
		if (pass.getValidUntil().compareTo(now) < 0) {
			throw new PassExpiredException();
		}
		if (pass.getTimeLeft() < 1) {
			throw new NoMoreTimesOnPassException();
		}
		pass.setTimeLeft(pass.getTimeLeft() - 1);
		entityManager.merge(pass);

		Entry entry = new Entry();
		entry.setEnteredOn(now);
		entry.setPass(pass);
		entry.setUser(user);
	}
}
