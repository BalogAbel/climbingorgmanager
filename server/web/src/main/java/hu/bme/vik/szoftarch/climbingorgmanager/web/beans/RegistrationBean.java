package hu.bme.vik.szoftarch.climbingorgmanager.web.beans;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EmailAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UsernameAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.UserData;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class RegistrationBean implements Serializable {
	@Getter
	@Setter
	private User user;

	@Getter
	@Setter
	private String password2;

	@Inject
	private UserManager userManager;

	@PostConstruct
	public void init() {
		user = new User();
		user.setUserData(new UserData());
	}

	public String register() {
		user.setRegisteredOn(new Date());
		user.setLastLoginOn(new Date());
		FacesContext facesContext = FacesContext.getCurrentInstance();

		try {
			userManager.addUser(user);
			return "registerSuccess.xhtml";
		} catch (EmailAlreadyRegisteredException e) {
			FacesMessage facesMessage = new FacesMessage("the given email address is already in use");
			facesContext.addMessage("register:email", facesMessage);
		} catch (UsernameAlreadyRegisteredException e) {
			FacesMessage facesMessage = new FacesMessage("the given username is already in use");
			facesContext.addMessage("register:username", facesMessage);
		}
		return "";
	}
}
