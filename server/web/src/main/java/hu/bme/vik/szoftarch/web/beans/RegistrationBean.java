package hu.bme.vik.szoftarch.web.beans;

import hu.bme.vik.szoftarch.entities.User;
import hu.bme.vik.szoftarch.entities.UserData;
import hu.bme.vik.szoftarch.exceptions.EmailAlreadyRegisteredException;
import hu.bme.vik.szoftarch.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.exceptions.UsernameAlreadyRegisteredException;
import hu.bme.vik.szoftarch.managers.UserManager;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@Named
@RequestScoped
public class RegistrationBean implements Serializable{
    @Getter @Setter
    private User user;

    @Getter @Setter
    private String password2;

    @Inject
    private UserManager userManager;

    @PostConstruct
    public void init(){
        user = new User();
        user.setUserData(new UserData());
    }

    public String register() {
        user.setRegisteredOn(new Date());
        user.setLastLoginOn(new Date());
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {
            userManager.addUser(user);
        } catch (EmailAlreadyRegisteredException e) {
            FacesMessage facesMessage = new FacesMessage("the given email address is already in use");
            facesContext.addMessage("register:email", facesMessage);
        } catch (UsernameAlreadyRegisteredException e) {
            FacesMessage facesMessage = new FacesMessage("the given username is already in use");
            facesContext.addMessage("register:username", facesMessage);
        }

        return "registerSuccess.xhtml";
    }
}
