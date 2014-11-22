package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"user", "equipment"})
@Entity
@NamedQueries({
		@NamedQuery(
				name = Rental.GET_ALL,
				query = "select r from Rental r"
		),
		@NamedQuery(
				name = Rental.GET_BY_ID,
				query = "select r from Rental r where r.id = :id"
		),
		@NamedQuery(
				name = Rental.GET_BY_USER,
				query = "select r from Rental r where r.user = :user"
		)
})
public class Rental implements Serializable {

	public static final String GET_ALL = "Rental.getAll";
	public static final String GET_BY_ID = "Rental.byId";
	public static final String GET_BY_USER = "Rental.byUser";

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@NotNull
	private User user;

	@ManyToOne
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
