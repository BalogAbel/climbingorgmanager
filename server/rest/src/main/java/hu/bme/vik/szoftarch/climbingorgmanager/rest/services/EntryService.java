package hu.bme.vik.szoftarch.climbingorgmanager.rest.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoMoreTimesOnPassException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchPassException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.PassExpiredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EntryManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.PassManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Path("/entries")
@Produces("application/json")
@Consumes("application/json")
public class EntryService {

	@Inject
	private EntryManager entryManager;
	@Inject
	private UserManager userManager;
	@Inject
	private PassManager passManager;

	@GET
	public List<Entry> getEntries() {
		return entryManager.getEntries();
	}

	@POST
	@Path("/ticket/{userId}")
	public Response enterWithTicket(@PathParam("userId") long userId) {
		User user;
		try {
			user = userManager.getUserById(userId);
		} catch (NoSuchUserException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user found for id.").build();
		}
		entryManager.enterWithTicket(user);
		return Response.status(Response.Status.OK).build();
	}

	@PUT
	@Path("/pass/{passId}/{userId}")
//	@Consumes("application/x-www-form-urlencoded")
	public Response enterWithPass(@PathParam("userId") long userId, @PathParam("passId") long passId) {
		User user;
		try {
			user = userManager.getUserById(userId);
		} catch (NoSuchUserException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user found for id.").build();
		}
		Pass pass;
		try {
			pass = passManager.getPass(passId);
		} catch (NoSuchPassException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No pass found for id.").build();
		}
		try {
			entryManager.enterWithPass(user, pass);
		} catch (PassExpiredException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Pass expired!").build();
		} catch (NoMoreTimesOnPassException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No more times on pass!").build();
		}
		return Response.status(Response.Status.OK).build();
	}

}
