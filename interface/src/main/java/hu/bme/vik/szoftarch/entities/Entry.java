package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Abel on 2014.11.02..
 */
@Entity
@Data
public class Entry {
    @Id @GeneratedValue
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date enteredOn;

    @ManyToOne
    @NotNull
    private Ticket ticket;

    @ManyToOne
    @NotNull
    private User user;
}
