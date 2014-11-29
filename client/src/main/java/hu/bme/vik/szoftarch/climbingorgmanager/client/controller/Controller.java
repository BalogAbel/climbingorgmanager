package hu.bme.vik.szoftarch.climbingorgmanager.client.controller;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners.ChangeListener;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.EditEquipmentFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.EditUserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.LoginFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.MainFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.PassChooserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.frame.UserChooserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EntriesTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EquipmentTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.UserTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.util.GsonMessageBodyHandler;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Entry;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.EquipmentType;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Rental;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Token;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.20..
 */
public class Controller {

	//	private static final String SERVER_URL = "http://climbingorgmanager-asztalosdani.rhcloud.com";
	private static final String SERVER_URL = "http://localhost:8082";

	private static Controller instance;

	private User selectedUser;
	private Token token;

	private List<EquipmentType> equipmentTypes;
	private List<User> users;

	private UserTableModel userTableModel;
	private EquipmentTableModel equipmentTableModel;
	private EntriesTableModel entriesTableModel;
	private MainFrame mainFrame;

	private List<ChangeListener> changeListeners;

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	private Controller() {
		changeListeners = new LinkedList<>();
	}

	public void setSelectedUser(long id) {
		for (User user : users) {
			if (user.getId() == id) {
				setSelectedUser(user);
				return;
			}
		}
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
		for (ChangeListener listener : changeListeners) {
			listener.onSelectedUserChanged(selectedUser);
		}
		if (selectedUser != null) {
			getActiveRentalsForUser();
			loadPassesForSelectedUser();
		}
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	public void bind(MainFrame mainFrame, UserTableModel userTableModel) {
		this.mainFrame = mainFrame;
		this.userTableModel = userTableModel;
		loadEquipmentTypes();
		loadEntries();
	}

	public void setEquipmentTableModel(EquipmentTableModel equipmentTableModel) {
		this.equipmentTableModel = equipmentTableModel;
	}

	public void setEntriesTableModel(EntriesTableModel entriesTableModel) {
		this.entriesTableModel = entriesTableModel;
	}

	private void setToken(Token token) {
		this.token = token;
	}

	public void login(final LoginFrame frame, String username, String password) {
		Form form = new Form();
		form.param("username", username);
		form.param("password", password);

		Client client = ClientBuilder.newClient().register(GsonMessageBodyHandler.class);
		ClientBuilder.newClient().register(GsonMessageBodyHandler.class);

		client.target(SERVER_URL + "/rest-1.0-SNAPSHOT/rest")
				.path("users/login").request(MediaType.APPLICATION_JSON_TYPE)
				.async().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
				new InvocationCallback<Response>() {
					@Override
					public void completed(Response response) {
						if (response.getStatus() == Response.Status.OK.getStatusCode()) {
							frame.dispose();
							setToken(response.readEntity(Token.class));
							MainFrame frame = new MainFrame(token.getUser());
							frame.setVisible(true);
						} else {
							String error = response.readEntity(String.class);
							frame.onLoginFailed(error);
						}
					}

					@Override
					public void failed(Throwable throwable) {
						throwable.printStackTrace();
						frame.onLoginFailed(throwable.toString());
					}
				});
	}

	public void logout() {
		this.token = null;
	}

	public List<User> getUsers() {
		return users;
	}

	public void loadUsers() {
		callGetService("users", new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				users = response.readEntity(new GenericType<List<User>>() {
				});
				userTableModel.setUsers(users);
			}
		});
	}

	private void loadEquipmentTypes() {
		callGetService("equipments/types", new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				equipmentTypes = response.readEntity(new GenericType<List<EquipmentType>>() {
				});
			}
		});
	}

	public List<EquipmentType> getEquipmentTypes() {
		return equipmentTypes;
	}

	public void loadEquipments() {
		callGetService("equipments", new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				List<Equipment> equipments = response.readEntity(new GenericType<List<Equipment>>() {
				});
				equipmentTableModel.setEquipments(equipments);
			}
		});
	}

	public void addNewUser(User user, final EditUserFrame source) {
		callPostService(source, "users", Entity.entity(user, MediaType.APPLICATION_JSON_TYPE), new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				source.dispose();
				loadUsers();
			}
		});
	}

	public void editUser(User user, final EditUserFrame source) {
		createInvoker("users").put(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE),
				new InvocationCallback<Response>() {
					@Override
					public void completed(Response response) {
						System.out.println(response);
						if (response.getStatus() == Response.Status.OK.getStatusCode()) {
							source.dispose();
							loadUsers();
						} else {
							String error = response.readEntity(String.class);
							JOptionPane.showMessageDialog(source, error, "Error", JOptionPane.ERROR_MESSAGE);
						}
					}

					@Override
					public void failed(Throwable throwable) {
						throwable.printStackTrace();
						String error = throwable.toString();
						JOptionPane.showMessageDialog(source, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				});
	}

	public void buyPass(int passId, final PassChooserFrame source) {
		int validMonths = 0;
		int timeLeft = 0;
		switch (passId) {
			case 2:
				validMonths = 2;
				timeLeft = 10;
				break;
			case 3:
				validMonths = 4;
				timeLeft = 20;
				break;
			case 4:
				validMonths = 6;
				timeLeft = 20;
				break;
		}

		Form form = new Form();
		form.param("userId", Long.toString(selectedUser.getId()));
		form.param("validMonths", Integer.toString(validMonths));
		form.param("timeLeft", Integer.toString(timeLeft));

		callPostService(source, "passes/buy", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
				new ResponseProcessor() {
					@Override
					public void processResponse(Response response) {
						loadPassesForSelectedUser();
						source.dispose();
					}
				});
	}

	public void rentEquipment(long equipmentId) {
		String path = "rentals/rent/" + selectedUser.getId() + "/" + equipmentId;
		callPostService(path, null, new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				JOptionPane.showMessageDialog(mainFrame, "Successfully rented!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				loadEquipments();
				getActiveRentalsForUser();
			}
		});
	}

	public void returnEquipment(long rentalId) {
		String path = "rentals/return/" + rentalId;
		callPostService(path, null, new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				JOptionPane.showMessageDialog(mainFrame, "Successfully returned!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				loadEquipments();
				getActiveRentalsForUser();
			}
		});
	}

	public void getActiveRentalsForUser() {
		String path = "rentals/active/" + selectedUser.getId();
		callGetService(path, new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				List<Rental> rentals = response.readEntity(new GenericType<List<Rental>>() {
				});
				for (ChangeListener listener : changeListeners) {
					listener.onRentalsLoaded(rentals);
				}
			}
		});
	}

	public void addEquipment(final EditEquipmentFrame source, Equipment equipment) {
		callPostService("equipments", Entity.entity(equipment, MediaType.APPLICATION_JSON_TYPE),
				new ResponseProcessor() {
					@Override
					public void processResponse(Response response) {
						source.dispose();
						loadEquipments();
					}
				});
	}

	public void editEquipment(final EditEquipmentFrame source, Equipment equipment) {
		callPutService("equipments", Entity.entity(equipment, MediaType.APPLICATION_JSON_TYPE),
				new ResponseProcessor() {
					@Override
					public void processResponse(Response response) {
						source.dispose();
						loadEquipments();
					}
				});
	}

	public void loadEntries() {
		callGetService("entries", new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				List<Entry> entries = response.readEntity(new GenericType<List<Entry>>() {
				});
				entriesTableModel.setEntries(entries);
			}
		});
	}

	public void loadPassesForSelectedUser() {
		String path = "passes/" + selectedUser.getId();
		callGetService(path, new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				List<Pass> passes = response.readEntity(new GenericType<List<Pass>>() {
				});
				for (ChangeListener listener : changeListeners) {
					listener.onPassesLoaded(passes);
				}
			}
		});
	}

	public void enterWithTicket() {
		enterWithTicket(selectedUser.getId());
	}

	public void enterWithTicket(long userId) {
		String path = "entries/ticket/" + userId;
		callPostService(path, null, new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				loadEntries();
				JOptionPane.showMessageDialog(mainFrame, "Ticket bought!", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	public void enterWithPass(final long userId, long passId) {
		String path = "entries/pass/" + passId + "/" + userId;
		Form form = new Form();
		form.param("userId", Long.toString(userId));
		form.param("passId", Long.toString(passId));
		callPutService(path, Entity.entity(form, MediaType.APPLICATION_JSON_TYPE),
				new ResponseProcessor() {
					@Override
					public void processResponse(Response response) {
						loadEntries();
						JOptionPane.showMessageDialog(mainFrame, "Pass used!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						if (userId != -1) {
							loadPassesForSelectedUser();
						}
					}
				});
	}

	public void enterWithPass(final UserChooserFrame source, List<User> selectedUsers, long passId) {
		String path = "entries/multipass/" + passId;
		callPostService(source, path, Entity.entity(selectedUsers, MediaType.APPLICATION_JSON_TYPE),
				new ResponseProcessor() {
					@Override
					public void processResponse(Response response) {
						loadEntries();
						loadPassesForSelectedUser();
						JOptionPane.showMessageDialog(source, "Pass used!", "Success", JOptionPane.INFORMATION_MESSAGE);
						source.dispose();
					}
				});
	}

	//------ SERVICE METHODS ------
	private void callGetService(String path, final ResponseProcessor responseProcessor) {
		createInvoker(path).get(new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					responseProcessor.processResponse(response);
				} else {
					String error = response.readEntity(String.class);
					JOptionPane.showMessageDialog(mainFrame, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void failed(Throwable throwable) {
				throwable.printStackTrace();
				String error = throwable.toString();
				JOptionPane.showMessageDialog(mainFrame, error, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private void callPostService(String path, Entity entity, final ResponseProcessor responseProcessor) {
		callPostService(mainFrame, path, entity, responseProcessor);
	}

	private void callPostService(final JFrame source, String path, Entity entity,
			final ResponseProcessor responseProcessor) {
		createInvoker(path).post(entity, new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					responseProcessor.processResponse(response);
				} else {
					String error = response.readEntity(String.class);
					JOptionPane.showMessageDialog(source, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void failed(Throwable throwable) {
				throwable.printStackTrace();
				String error = throwable.toString();
				JOptionPane.showMessageDialog(source, error, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private void callPutService(String path, Entity entity, final ResponseProcessor responseProcessor) {
		createInvoker(path).put(entity, new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					responseProcessor.processResponse(response);
				} else {
					String error = response.readEntity(String.class);
					JOptionPane.showMessageDialog(mainFrame, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void failed(Throwable throwable) {
				throwable.printStackTrace();
				String error = throwable.toString();
				JOptionPane.showMessageDialog(mainFrame, error, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private AsyncInvoker createInvoker(String path) {
		Client client = ClientBuilder.newClient().register(GsonMessageBodyHandler.class);
		ClientBuilder.newClient().register(GsonMessageBodyHandler.class);
		return client.target(SERVER_URL + "/rest-1.0-SNAPSHOT/rest")
				.path(path).request(MediaType.APPLICATION_JSON_TYPE)
				.header("Authorization", token.getToken())
				.async();
	}

	private interface ResponseProcessor {
		public void processResponse(Response response);
	}
}
