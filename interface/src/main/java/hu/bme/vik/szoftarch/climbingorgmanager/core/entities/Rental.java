package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class Rental implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@NotNull
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	@NotNull
	private Equipment equipment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date rentedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date rentedUntil;

	@Temporal(TemporalType.TIMESTAMP)
	private Date returnedOn;

}
