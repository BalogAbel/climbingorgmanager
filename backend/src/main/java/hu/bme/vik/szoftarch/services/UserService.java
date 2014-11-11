package hu.bme.vik.szoftarch.services;

import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import hu.bme.vik.szoftarch.managers.UserManager;

@Path("/users")
@Produces("application/json")
@Consumes("application/json")
public class UserService {

	@Inject
	private UserManager manager;

	@GET
	public String getUsers() {
		return manager.getUsers().toString();
	}
}
