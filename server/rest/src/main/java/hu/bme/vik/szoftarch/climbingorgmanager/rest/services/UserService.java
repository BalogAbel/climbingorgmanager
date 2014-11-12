package hu.bme.vik.szoftarch.climbingorgmanager.rest.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import hu.bme.vik.szoftarch.exceptions.EmailAlreadyRegisteredException;
import hu.bme.vik.szoftarch.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.exceptions.UsernameAlreadyRegisteredException;
import hu.bme.vik.szoftarch.managers.UserManager;

@Path("/users")
@Produces("application/json")
@Consumes("application/json")
public class UserService {

	@Inject
	private UserManager manager;

	@GET
	public List<User> getUsers() {
		return manager.getUsers();
	}

	@POST
	public String addUser(User user) {
		try {
			manager.addUser(user);
		} catch (UsernameAlreadyRegisteredException e) {
			return "Username already registered";
		} catch (EmailAlreadyRegisteredException e) {
			return "Email already registered!";
		}
		return "Hello " + user.getUserName();
	}

	@PUT
	public Response editUser(User user) {
		manager.editUser(user);
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
	@Path("/{id}")
	public Response removeUser(@PathParam("id") long id) {
		User user = null;
		try {
			user = manager.getUserById(id);
			manager.removeUser(user);
		} catch (NoSuchUserException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user found for id.").build();
		}
		return Response.status(Response.Status.OK).entity(user).build();
	}
}
