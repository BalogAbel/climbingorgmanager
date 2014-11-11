package hu.bme.vik.szoftarch.managers;

import org.jasypt.util.password.BasicPasswordEncryptor;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import hu.bme.vik.szoftarch.entities.User;
import hu.bme.vik.szoftarch.exceptions.BadLoginCredentialsException;
import hu.bme.vik.szoftarch.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.exceptions.UsernameAlreadyRegisteredException;

@Local
@Stateless
public class UserManager {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public User login(String username, String password) throws BadLoginCredentialsException {
		TypedQuery<User> query = entityManager.createNamedQuery(User.GET_BY_USERNAME, User.class);
		query.setParameter("username", username);
		List<User> users = query.getResultList();
		if (users.size() != 1) {
			throw new BadLoginCredentialsException();
		}

		User user = users.get(0);

		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		if (!passwordEncryptor.checkPassword(password, user.getPassword())) {
			throw new BadLoginCredentialsException();
		}

		return user;
	}

	public void addUser(User user) throws UsernameAlreadyRegisteredException {
		TypedQuery<User> query = entityManager.createNamedQuery(User.GET_BY_USERNAME, User.class);
		query.setParameter("username", user.getUserName());
		List<User> users = query.getResultList();
		if (users.size() > 0) {
			throw new UsernameAlreadyRegisteredException();
		}
		entityManager.merge(user);
	}

	public User getUserById(long id) throws NoSuchUserException {
		TypedQuery<User> query = entityManager.createNamedQuery(User.GET_BY_ID, User.class);
		query.setParameter("id", id);
		List<User> users = query.getResultList();
		if (users.size() != 1) {
			throw new NoSuchUserException();
		}

		return users.get(0);
	}

	public List<User> getUsers() {
		return entityManager.createNamedQuery(User.GET_ALL, User.class).getResultList();
	}

	public void editUser(User user) {
		entityManager.merge(user);
	}

	public void removeUser(User user) {
		User attachedUser = entityManager.merge(user);
		entityManager.remove(attachedUser);
	}

}
