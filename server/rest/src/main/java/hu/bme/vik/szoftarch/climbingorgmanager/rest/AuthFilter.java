package hu.bme.vik.szoftarch.climbingorgmanager.rest;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.BadLoginCredentialsException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Token;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AuthFilter implements ContainerRequestFilter {

	@Inject
	private UserManager userManager;

	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		String path = containerRequestContext.getUriInfo().getPath();
		if (!((path.startsWith("/users/login") && containerRequestContext.getMethod().equals("POST"))
				|| (path.startsWith("/users/add") && containerRequestContext.getMethod().equals("POST")))) {

			String tokenStr = containerRequestContext.getHeaderString("Authorization");
			if(tokenStr == null) tokenStr = "";
			try {
				userManager.login(tokenStr);
			} catch (BadLoginCredentialsException e) {
				//fallback to test case
				//TODO: just for testing porpuses
				if(!tokenStr.equals("magic")) {
					throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				}
			}



		}

	}

}
