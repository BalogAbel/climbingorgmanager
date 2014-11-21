package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@NamedQueries({
		@NamedQuery(
				name = EquipmentType.GET_ALL,
				query = "select et from EquipmentType et"
		)
})
public class EquipmentType implements Serializable {
	public static final String GET_ALL = "EquipmentType.getAll";

	@Id
	@GeneratedValue
	private Long id;

	@Size(min = 4, max = 40)
	@NotNull
	private String name;

	@Size(max = 256)
	@NotNull
	private String description;
}
