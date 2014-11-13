package hu.bme.vik.szoftarch.climbingorgmanager.rest.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.BadLoginCredentialsException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EmailAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UsernameAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Token;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Path("/users")
@Produces("application/json")
@Consumes("application/json")
public class UserService {

	@Inject
	private UserManager manager;

	@GET
	@Path("/login")
	public Response login(@QueryParam("username") String username,
					   @QueryParam("password") String password) {

		try {
			Token token = manager.loginRest(username, password);
			return Response.ok().entity(token).build();
		} catch (BadLoginCredentialsException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

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
