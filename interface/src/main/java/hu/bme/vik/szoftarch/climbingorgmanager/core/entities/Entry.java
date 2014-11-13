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

@Entity
@Data
@NamedQueries({
		@NamedQuery(
				name = Entry.GET_ALL,
				query = "select e from Entry e"
		),
		@NamedQuery(
				name = Entry.GET_BY_ID,
				query = "select e from Entry e where e.id = :id"
		)
})
public class Entry implements Serializable {

	public static final String GET_ALL = "Entry.getAll";
	public static final String GET_BY_ID = "Entry.byId";

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date enteredOn;

	@ManyToOne
	@NotNull
	private Pass pass;

	@ManyToOne
	@NotNull
	private User user;
}
