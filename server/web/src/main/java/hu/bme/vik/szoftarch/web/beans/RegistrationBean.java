package hu.bme.vik.szoftarch.web.beans;

import hu.bme.vik.szoftarch.entities.User;
import hu.bme.vik.szoftarch.entities.UserData;
import hu.bme.vik.szoftarch.managers.UserManager;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

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

    public void register() {
        System.out.println(user.toString());
        System.out.println(password2);
    }
}
