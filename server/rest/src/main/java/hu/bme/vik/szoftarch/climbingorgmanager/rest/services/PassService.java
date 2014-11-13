package hu.bme.vik.szoftarch.climbingorgmanager.rest.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.PassManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Path("/passes")
@Produces("application/json")
public class PassService {

	@Inject
	private UserManager userManager;
	@Inject
	private PassManager passManager;

	@POST
	@Path("/buy")
	@Consumes("application/x-www-form-urlencoded")
	public Response buyPass(@FormParam("userId") long userId, @FormParam("validMonths") int validMonths,
			@FormParam("timeLeft") int timeLeft) {
		try {
			User user = userManager.getUserById(userId);

			passManager.buyPass(user, timeLeft, validMonths);
			return Response.status(Response.Status.OK).build();
		} catch (NoSuchUserException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user found for id.").build();
		}
	}

	@GET
	public Response getPassesForUser(@QueryParam("userId") long userId) {
		try {
			User user = userManager.getUserById(userId);

			List<Pass> passes = passManager.getPassesForUser(user); //TODO remove unnecessary fields
			return Response.status(Response.Status.OK).entity(passes).build();
		} catch (NoSuchUserException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user found for id.").build();
		}
	}
}
