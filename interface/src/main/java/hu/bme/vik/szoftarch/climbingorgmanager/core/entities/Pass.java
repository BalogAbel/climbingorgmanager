package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class Pass extends Ticket implements Serializable {
    private int timeLeft;
}
