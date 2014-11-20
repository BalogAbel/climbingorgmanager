package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
		@NamedQuery(
				name = User.GET_BY_USERNAME,
				query = "select u from User u where u.userName = :username"
		),
		@NamedQuery(
				name = User.GET_BY_EMAIL,
				query = "select u from User u where u.email = :email"
		),
		@NamedQuery(
				name = User.GET_ALL,
				query = "select u from User u"
		),
		@NamedQuery(
				name = User.GET_BY_ID,
				query = "select u from User u where u.id = :id"
		)
})
public class User implements Serializable {
	public static final String GET_BY_USERNAME = "User.getByUsername";
	public static final String GET_BY_EMAIL = "User.getByEmail";
	public static final String GET_ALL = "User.getAll";
	public static final String GET_BY_ID = "User.getById";

	@Id
	@GeneratedValue
	private Long id;

	@Size(min = 4, max = 30)
	@NotNull
	private String userName;

	@Size(min = 6, max = 256)
	@NotNull
	private String password;

	@Size(min = 6, max = 256)
	@NotNull
	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date lastLoginOn;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date registeredOn;

	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private UserData userData;

	private boolean isAdmin = false;

	@Transient
	@Setter(AccessLevel.PRIVATE)
	private boolean recognizedMember;

	@PostLoad
	private void onPostLoad() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		recognizedMember = registeredOn.compareTo(cal.getTime()) < 0;
	}

}
