package hu.bme.vik.szoftarch.climbingorgamanager.backend.managers;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.BadLoginCredentialsException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EmailAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UsernameAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Token;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import org.jasypt.util.password.BasicPasswordEncryptor;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Local
@Stateless
public class UserManager implements Serializable {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public Token login(String token) throws BadLoginCredentialsException {
		TypedQuery<Token> query = entityManager.createNamedQuery(Token.GET_BY_TOKEN, Token.class);
		query.setParameter("token", token);
		List<Token> tokens = query.getResultList();
		if (tokens.size() != 1) {
			throw new BadLoginCredentialsException();
		}
		return tokens.get(0);
	}

	public Token loginRest(String username, String password) throws BadLoginCredentialsException {
		User user = login(username, password);
		if (!user.isAdmin()) throw new BadLoginCredentialsException();
		Token token = new Token();

		SecureRandom random = new SecureRandom();
		token.setToken(new BigInteger(130, random).toString(32));
		token.setUser(user);
		entityManager.persist(token);
		return token;
	}

	public User login(String username, String password) throws BadLoginCredentialsException {
		TypedQuery<User> query = entityManager.createNamedQuery(User.GET_BY_USERNAME, User.class);
		query.setParameter("username", username);
		List<User> users = query.getResultList();
		if (users.size() != 1) {
			throw new BadLoginCredentialsException();
		}

		User user = users.get(0);
		user.setLastLoginOn(new Date());
		entityManager.persist(user);

		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		if (!passwordEncryptor.checkPassword(password, user.getPassword())) {
			throw new BadLoginCredentialsException();
		}

		return user;
	}

	public void addUser(User user) throws UsernameAlreadyRegisteredException, EmailAlreadyRegisteredException {
		TypedQuery<User> query = entityManager.createNamedQuery(User.GET_BY_USERNAME, User.class);
		query.setParameter("username", user.getUserName());
		List<User> users = query.getResultList();
		if (users.size() > 0) {
			throw new UsernameAlreadyRegisteredException();
		}
		query = entityManager.createNamedQuery(User.GET_BY_EMAIL, User.class);
		query.setParameter("email", user.getEmail());
		users = query.getResultList();
		if (users.size() > 0) {
			throw new EmailAlreadyRegisteredException();
		}
		BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
		String encryptedPassword = encryptor.encryptPassword(user.getPassword());
		user.setPassword(encryptedPassword);
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
