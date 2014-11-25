package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.EmailAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.UsernameAlreadyRegisteredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.UserData;
import hu.bme.vik.szoftarch.climbingorgmanager.web.beans.AuthBean;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

@Named
@RequestScoped
public class MemberBean implements Serializable {

	@Inject
	private UserManager userManager;

	@Inject
	private AuthBean authBean;

	@Getter
	@Setter
	private User user;

	@PostConstruct
	public void init() {
		String idStr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("id");
		if (idStr == null) {
			user = new User();
			user.setUserData(new UserData());
			return;
		}
		try {
			Long id = Long.valueOf(idStr);
			user = userManager.getUserById(id);
		} catch (NoSuchUserException | NumberFormatException e) {
			System.out.println("No user found with the given id - " + idStr);
		}
	}

	public String save() {
		userManager.editUser(user);
		return "member.xhtml?id=" + user.getId();
	}

	public String create() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		user.setRegisteredOn(new Date());
		user.setLastLoginOn(new Date());
		try {
			userManager.addUser(user);
			return "member.xhtml?id=" + user.getId();
		} catch (EmailAlreadyRegisteredException e) {
			FacesMessage facesMessage = new FacesMessage("the given email address is already in use");
			facesContext.addMessage("editMember:email", facesMessage);
		} catch (UsernameAlreadyRegisteredException e) {
			FacesMessage facesMessage = new FacesMessage("the given username is already in use");
			facesContext.addMessage("editMember:username", facesMessage);
		}
		return null;
	}
}
