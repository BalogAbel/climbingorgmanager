package hu.bme.vik.szoftarch.climbingorgmanager.web.beans.admin;

import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoMoreTimesOnPassException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.NoSuchUserException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.exceptions.PassExpiredException;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.EntryManager;
import hu.bme.vik.szoftarch.climbingorgamanager.backend.managers.UserManager;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EnterBean implements Serializable {
	@Inject
	private EntryManager entryManager;
	@Inject
	private UserManager userManager;

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
	private String message;

	@PostConstruct
	public void init() {
		String idStr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("id");
		try {
			Long id = Long.valueOf(idStr);
			user = userManager.getUserById(id);
			passes = entryManager.getPasses(user);
			entries = entryManager.getEntries(user);
		} catch (NoSuchUserException | NumberFormatException e) {
			System.out.println("No user found with the given id - " + idStr);
		}
	}


	public void usePass() {
		try {
			entryManager.enterWithPass(user, selectedPass);
			message = "Success";
			passes = entryManager.getPasses(user);
			entries = entryManager.getEntries(user);
		} catch (PassExpiredException e) {
			message = "The pass has expired";
		} catch (NoMoreTimesOnPassException e) {
			message = "No time left on the pass";
		}
	}
}
