package hu.bme.vik.szoftarch.services;

import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import hu.bme.vik.szoftarch.managers.UserManager;

@Path("/users")
public class UserService {

	@Inject
	UserManager manager;

	@GET
	public String getUsers() {
		return manager.getUsers().toString();
	}
}
