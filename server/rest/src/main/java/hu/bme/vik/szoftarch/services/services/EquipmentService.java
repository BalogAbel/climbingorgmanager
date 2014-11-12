package hu.bme.vik.szoftarch.services.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.entities.Equipment;
import hu.bme.vik.szoftarch.managers.EquipmentManager;

@Path("/equipments")
@Produces("application/json")
@Consumes("application/json")
public class EquipmentService {

	@Inject
	private EquipmentManager equipmentManager;

	@GET
	public List<Equipment> getEquipments() {
		return equipmentManager.getEquipments();
	}

	@POST
	public Response addEquipment(Equipment equipment) {
		equipmentManager.addEquipment(equipment);
		return Response.status(Response.Status.OK).build();
	}



}
