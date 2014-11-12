package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import java.io.Serializable;
import java.util.Date;

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
@Entity
@Data
public class Entry implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date enteredOn;

	@ManyToOne
	@NotNull
	private Ticket ticket;

	@ManyToOne
	@NotNull
	private User user;
}
