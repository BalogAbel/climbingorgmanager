package hu.bme.vik.szoftarch.climbingorgamanager.backend.managers;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

		Date now = new Date();

		Entry entry = new Entry();
		entry.setEnteredOn(now);
		entry.setPass(null);
		entry.setUser(managedUser);

		entityManager.merge(entry);
	}

	public void enterWithPass(User user, Pass pass) throws PassExpiredException, NoMoreTimesOnPassException {
		User managedUser = entityManager.merge(user);
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
		entry.setUser(managedUser);
		entityManager.persist(entry);
	}

	public void guestEntry() {
		Date now = new Date();

		Entry entry = new Entry();
		entry.setEnteredOn(now);
		entry.setPass(null);
		entry.setUser(null);

		entityManager.merge(entry);
	}

	public void guestEntryWithPass(Pass pass) throws NoMoreTimesOnPassException, PassExpiredException {
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
		entry.setUser(null);
		entityManager.persist(entry);
	}

	public List<Pass> getPasses(User user) {
		TypedQuery<Pass> query = entityManager.createNamedQuery(Pass.GET_BY_USER, Pass.class);
		query.setParameter("owner", user);
		return query.getResultList();
	}

	public List<Entry> getEntries(User user) {
		TypedQuery<Entry> query = entityManager.createNamedQuery(Entry.GET_BY_USER, Entry.class);
		query.setParameter("user", user);
		return query.getResultList();
	}
}
