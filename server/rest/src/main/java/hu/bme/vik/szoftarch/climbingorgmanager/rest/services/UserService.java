package hu.bme.vik.szoftarch.climbingorgmanager.rest.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.BadLoginCredentialsException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EmailAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UserNotAuthorizedException;
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

	@POST
	@Path("/login")
	@Consumes("application/x-www-form-urlencoded")
	public Response login(@FormParam("username") String username, @FormParam("password") String password) {

		try {
			Token token = manager.loginRest(username, password);
			return Response.ok().entity(token).build();
		} catch (BadLoginCredentialsException e) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Username or password incorrect.").build();
		} catch (UserNotAuthorizedException e) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized to log in.").build();
		}

	}

	@GET
	public List<User> getUsers() {
		return manager.getUsers();
	}

	@POST
	public Response addUser(User user) {
		try {
			manager.addUser(user);
		} catch (UsernameAlreadyRegisteredException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Username already registered").build();
		} catch (EmailAlreadyRegisteredException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Email already registered!").build();
		}
		return Response.status(Response.Status.OK).build();
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
