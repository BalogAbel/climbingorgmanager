package hu.bme.vik.szoftarch.climbingorgmanager.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Data;

/**
 * Created by Abel on 2014.11.02..
 */
@Data
@Entity
public class Pass extends Ticket implements Serializable {
	private int timeLeft;
}
