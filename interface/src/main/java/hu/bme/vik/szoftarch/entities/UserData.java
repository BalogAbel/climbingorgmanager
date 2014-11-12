package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class UserData implements Serializable {

    @Id @GeneratedValue
    private Long id;

    @Size(min = 4, max = 40)
    @NotNull
    private String firstName;

    @Size(min = 4, max = 40)
    @NotNull
    private String lastName;

    @Size(min = 4, max = 200)
    @NotNull
    private String address;
}
