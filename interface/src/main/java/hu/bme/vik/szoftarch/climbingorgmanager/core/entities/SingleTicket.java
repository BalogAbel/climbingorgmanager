package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by Abel on 2014.11.02..
 */
@Entity
@Data
public class SingleTicket extends Ticket implements Serializable {
}
