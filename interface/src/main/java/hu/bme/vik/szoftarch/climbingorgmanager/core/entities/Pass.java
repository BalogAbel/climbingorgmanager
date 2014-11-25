package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
@NamedQueries({
		@NamedQuery(
				name = Pass.GET_ALL,
				query = "select p from Pass p"
		),
		@NamedQuery(
				name = Pass.GET_BY_ID,
				query = "select p from Pass p where p.id = :id"
		),
		@NamedQuery(
				name = Pass.GET_BY_USER,
				query = "select p from Pass p where p.owner = :owner"
		)
})
public class Pass implements Serializable {

	public static final String GET_ALL = "Pass.getAll";
	public static final String GET_BY_ID = "Pass.byId";
	public static final String GET_BY_USER = "Pass.byUser";

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User owner;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date boughtOn;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date validUntil;

	private int timeLeft;
}
