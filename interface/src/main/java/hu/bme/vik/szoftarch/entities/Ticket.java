package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class Ticket {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User owner;

    @Temporal(TemporalType.TIMESTAMP)
    private Date boughtOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date validUntil;
}
