package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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
		),
		@NamedQuery(
				name = Entry.GET_BY_USER,
				query = "select e from Entry e where e.user = :user"
		)
})
public class Entry implements Serializable {

	public static final String GET_ALL = "Entry.getAll";
	public static final String GET_BY_ID = "Entry.byId";
	public static final String GET_BY_USER = "Entry.byUser";

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
