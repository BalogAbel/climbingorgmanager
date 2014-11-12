package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class UserData implements Serializable {

	@Id
	@GeneratedValue
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
