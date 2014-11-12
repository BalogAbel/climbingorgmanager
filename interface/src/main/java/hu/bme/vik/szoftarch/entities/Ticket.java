package hu.bme.vik.szoftarch.entities;

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
public class Ticket implements Serializable {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @NotNull
    private User owner;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date boughtOn;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date validUntil;
}
