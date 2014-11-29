package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoMoreTimesOnPassException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.PassExpiredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EntryManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.PassManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class EnterBean implements Serializable {
	@Inject
	private EntryManager entryManager;
	@Inject
	private UserManager userManager;
	@Inject
	private PassManager passManager;

	@Getter
	private User user;

	@Getter
	@Setter
	private Pass selectedPass;

	@Getter
	private List<Pass> passes;

	@Getter
	private List<Entry> entries;

	@Getter
	@Setter
	private int validMonth;

	@Getter
	@Setter
	private int timeLeft;

	@Getter
	private String message;

	@PostConstruct
	public void init() {
		String idStr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("id");
		try {
			Long id = Long.valueOf(idStr);
			user = userManager.getUserById(id);
			updatePasses();
		} catch (NoSuchUserException | NumberFormatException e) {
			System.out.println("No user found with the given id - " + idStr);
		}
	}

	private void updatePasses() {
		passes = entryManager.getPasses(user);

		/**
		 * a negative integer, zero, or a positive integer as the
		 * first argument is less than, equal to, or greater than the
		 *  second.
		 */
		Collections.sort(passes, new Comparator<Pass>() {

			@Override
			public int compare(Pass o1, Pass o2) {
				if (o1.getValidUntil().after(o2.getValidUntil())) {
					if (o1.getTimeLeft() == 0) return -1;
					if (o1.getValidUntil().after(new Date())) return -1;
				}
				return 1;
			}
		});
		entries = entryManager.getEntries(user);
		Collections.sort(entries, new Comparator<Entry>() {
			@Override
			public int compare(Entry o1, Entry o2) {
				return o2.getEnteredOn().compareTo(o1.getEnteredOn());
			}
		});
	}


	public void usePass() {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			entryManager.enterWithPass(user, selectedPass);
			updatePasses();
			context.addMessage("entry", new FacesMessage("Successful", "Entry with pass was successful"));
		} catch (PassExpiredException e) {
			context.addMessage("entry", new FacesMessage("Error", "The pass has been expired"));
		} catch (NoMoreTimesOnPassException e) {
			context.addMessage("entry", new FacesMessage("Error", "There is no time left on the pass"));
		}
	}

	public void enterWithTicket() {
		entryManager.enterWithTicket(user);
		updatePasses();
		FacesContext.getCurrentInstance().addMessage("entry", new FacesMessage("Successful", "Entry with ticket was successful"));
	}

	public void addPass() {
		passManager.buyPass(user, timeLeft, validMonth);
		timeLeft = 0;
		validMonth = 0;
		updatePasses();
		FacesContext.getCurrentInstance().addMessage("entry", new FacesMessage("Successful", "Entry with ticket was successful"));

	}
}
