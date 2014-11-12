package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NamedQueries({
        @NamedQuery(
                name = User.GET_BY_USERNAME,
                query = "select u from User u where u.userName = :username"
        ),
        @NamedQuery(
                name = User.GET_BY_EMAIL,
                query = "select u from User u where u.email = :email"
        ),
        @NamedQuery(
                name = User.GET_ALL,
                query = "select u from User u"
        ),
        @NamedQuery(
                name = User.GET_BY_ID,
                query = "select u from User u where u.id = :id"
        )
})
public class User {
    public static final String GET_BY_USERNAME = "User.getByUsername";
    public static final String GET_BY_EMAIL = "User.getByEmail";
    public static final String GET_ALL = "User.getAll";
    public static final String GET_BY_ID = "User.getById";

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 4, max = 30)
    private String userName;

    @Size(min = 6, max = 256)
    private String password;

    @Size(min = 6, max = 256)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredOn;

    @Size(min = 1, max = 256)
    private String salt;

    @OneToOne
    private UserData userData;


}
