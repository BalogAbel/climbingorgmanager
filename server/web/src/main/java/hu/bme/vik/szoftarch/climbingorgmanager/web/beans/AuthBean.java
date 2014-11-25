package hu.bme.vik.szoftarch.climbingorgmanager.web.beans;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.BadLoginCredentialsException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.jasypt.util.password.BasicPasswordEncryptor;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class AuthBean implements Serializable {

	@Getter
	@Setter
	private User user;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String password;

	@Getter
	@Setter
	private String newPassword;

	@Inject
	private UserManager userManager;

	public String login() {
		try {
			this.user = userManager.login(username, password);
			username = "";
			password = "";
			return "app/index.xhtml?faces-redirect=true";
		} catch (BadLoginCredentialsException e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("The username or password is wrong");
			facesContext.addMessage("loginForm", facesMessage);
		}
		return "";

	}

	public void logout() throws IOException {
		user = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}


	public String save() {
		userManager.editUser(user);
		return "editProfileSuccess.xhtml";
	}

	public String changePassword() {
		try {
			user = userManager.login(user.getUserName(), password);
			BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
			user.setPassword(encryptor.encryptPassword(newPassword));
			userManager.editUser(user);
			newPassword = "";
			password = "";
		} catch (BadLoginCredentialsException e) {
			FacesMessage facesMessage = new FacesMessage("the given password is not correct");
			FacesContext.getCurrentInstance().addMessage("changePassword:currentPassword", facesMessage);
			return "";
		}
		return "editProfileSuccess.xhtml";
	}

}
