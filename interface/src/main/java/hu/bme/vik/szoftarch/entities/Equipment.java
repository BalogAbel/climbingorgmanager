package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class Equipment implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 4, max = 20)
    @NotNull
    private String accessionNumber;

    @Size(min = 5, max = 256)
    @NotNull
    private String description;

    @Size(min = 3, max = 40)
    @NotNull
    private String name;

    @ManyToOne
    @NotNull
    private EquipmentType equipmentType;


}
