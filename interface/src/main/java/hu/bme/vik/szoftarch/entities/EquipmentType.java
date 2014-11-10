package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class EquipmentType {
    @Id @GeneratedValue
    private Long id;

    @Size(min = 4, max = 40)
    private String name;

    @Size(max = 256)
    private String description;
}
