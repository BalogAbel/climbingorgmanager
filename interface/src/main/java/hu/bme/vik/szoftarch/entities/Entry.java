package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.*;
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
    private Date enteredOn;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private User user;
}
