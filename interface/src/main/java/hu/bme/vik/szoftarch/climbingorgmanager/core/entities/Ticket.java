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
@Data
@Entity
public class Ticket implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@NotNull
	private User owner;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date boughtOn;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date validUntil;
}
