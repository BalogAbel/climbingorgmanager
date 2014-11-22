package hu.bme.vik.szoftarch.climbingorgmanager.rest.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EquipmentAlreadyRentedException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchEquipmentException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchRentalException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UserNotRecognizedMemberException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EquipmentManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.RentalManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
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
	@Path("/rent/{userId}/{equipmentId}")
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
		try {
			rentalManager.rentEquipment(user, equipment);
		} catch (EquipmentAlreadyRentedException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Equipment already rented.").build();
		} catch (UserNotRecognizedMemberException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User not recognized as member.").build();
		}
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/return/{rentalId}")
	public Response returnEquipment(@PathParam("rentalId") long rentalId) {
		Rental rental;
		try {
			rental = rentalManager.getRental(rentalId);
		} catch (NoSuchRentalException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No rental found for id.").build();
		}
		rentalManager.returnEquipment(rental);
		return Response.status(Response.Status.OK).build();
	}

	@GET
	public List<Rental> getRentals() {
		List<Rental> rentals = rentalManager.getRentals();
		for (Rental rental : rentals) {
			Equipment equipment = rental.getEquipment();
			equipment.setActualRental(null);
		}
		return rentals;
	}

	@GET
	@Path("{userId}")
	public Response getRentals(@PathParam("userId") long userId) {
		try {
			User user = userManager.getUserById(userId);
			List<Rental> rentals = rentalManager.getRentalsForUser(user);
			for (Rental rental : rentals) {
				Equipment equipment = rental.getEquipment();
				equipment.setActualRental(null);
			}
			return Response.status(Response.Status.OK).entity(rentals).build();
		} catch (NoSuchUserException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No user found for id.").build();
		}
	}
}
