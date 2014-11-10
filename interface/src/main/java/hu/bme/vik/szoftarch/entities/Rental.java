package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class Rental {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Equipment equipment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date rentedOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date rentedUntil;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnedOn;

}
