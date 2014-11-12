package hu.bme.vik.szoftarch.climbingorgmanager.rest.services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchEquipmentException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.RentalManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

@Path("/rentals")
@Produces("application/json")
@Consumes("application/json")
public class RentalService {

	@Inject
	private RentalManager rentalManager;
	@Inject
	private UserManager userManager;
	@Inject
	private EquipmentManager equipmentManager;

	@POST
	@Path("/{userId}/{equipmentId}")
	public Response rentEquipment(@PathParam("userId") long userId, @PathParam("equipmentId") long equipmentId) {
		User user;
		try {
			user = userManager.getUserById(userId);
		} catch (NoSuchUserException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user found for id.").build();
		}
		Equipment equipment;
		try {
			equipment = equipmentManager.getEquipmentById(equipmentId);
		} catch (NoSuchEquipmentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No equipment found for id.").build();
		}
		rentalManager.rentEquipment(user, equipment);
		return Response.status(Response.Status.OK).build();
	}
}
