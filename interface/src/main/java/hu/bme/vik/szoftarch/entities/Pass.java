package hu.bme.vik.szoftarch.entities;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class Pass extends Ticket {
    private int timeLeft;
}