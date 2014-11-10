package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Abel on 2014.11.02..
 */
@Entity
@Data
public class User {
    @Id @GeneratedValue
    private Long id;

    @Size(min = 4, max = 30)
    private String userName;

    @Size(min = 7, max = 256)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredOn;

    @Size(min = 1, max = 256)
    private String salt;

    @OneToOne
    private UserData userData;


}
