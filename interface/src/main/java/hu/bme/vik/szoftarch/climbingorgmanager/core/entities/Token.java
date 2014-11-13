package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@NamedQueries({
		@NamedQuery(
				name = Token.GET_BY_TOKEN,
				query = "select t from Token t where t.token = :token"
		)
})
public class Token {
	public static final String GET_BY_TOKEN = "Token.GetByToken";

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User user;

	@Size(min=10, max = 40)
	private String token;
}
