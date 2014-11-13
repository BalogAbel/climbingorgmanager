package hu.bme.vik.szoftarch.climbingorgamanager.backend.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchPassException;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Local
@Stateless
public class PassManager {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public List<Pass> getPasses() {
		return entityManager.createNamedQuery(Pass.GET_ALL, Pass.class).getResultList();
	}

	public List<Pass> getPassesForUser(User user) {
		TypedQuery<Pass> query = entityManager.createNamedQuery(Pass.GET_BY_USER, Pass.class);
		query.setParameter("owner", user);
		return query.getResultList();
	}

	public Pass getPass(long passId) throws NoSuchPassException {
		TypedQuery<Pass> query = entityManager.createNamedQuery(Pass.GET_BY_ID, Pass.class);
		query.setParameter("id", passId);
		List<Pass> passes = query.getResultList();
		if (passes.size() != 1) {
			throw new NoSuchPassException();
		}
		return passes.get(0);
	}

	public void buyPass(User user, int timeLeft, int validMonths) {
		User managedUser = entityManager.merge(user);

		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, validMonths);
		Date validUntil = calendar.getTime();

		Pass pass = new Pass();
		pass.setBoughtOn(now);
		pass.setOwner(managedUser);
		pass.setTimeLeft(timeLeft);
		pass.setValidUntil(validUntil);
		entityManager.merge(pass);
	}

}
