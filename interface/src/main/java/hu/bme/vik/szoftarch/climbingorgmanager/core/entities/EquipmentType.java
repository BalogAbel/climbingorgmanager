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
		),
		@NamedQuery(
				name = EquipmentType.GET_BY_ID,
				query = "select et from EquipmentType et where et.id = :id"
		)
})
public class EquipmentType implements Serializable {
	public static final String GET_ALL = "EquipmentType.getAll";
	public static final String GET_BY_ID = "EquipmentType.getById";

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
