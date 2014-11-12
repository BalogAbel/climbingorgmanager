package hu.bme.vik.szoftarch.web.beans;

import hu.bme.vik.szoftarch.entities.User;
import hu.bme.vik.szoftarch.exceptions.BadLoginCredentialsException;
import hu.bme.vik.szoftarch.managers.UserManager;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
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

}
