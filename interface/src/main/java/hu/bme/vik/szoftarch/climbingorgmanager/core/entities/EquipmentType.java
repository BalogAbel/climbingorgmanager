package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class EquipmentType implements Serializable {
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
