package hu.bme.vik.szoftarch.climbingorgmanager.client.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

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

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners.RentalsForSelectedUserLoadedListener;
import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.listeners.SelectedUserChangeListener;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.EditEquipmentFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.EditUserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.MainFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.gui.PassChooserFrame;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EntriesTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EquipmentTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.PassesTableModel;
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

	private UserTableModel userTableModel;
	private PassesTableModel passesTableModel;
	private EquipmentTableModel equipmentTableModel;
	private EntriesTableModel entriesTableModel;
	private MainFrame mainFrame;

	private List<RentalsForSelectedUserLoadedListener> rentalsForSelectedUserLoadedListeners;
	private List<SelectedUserChangeListener> selectedUserChangeListeners;

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	private Controller() {
		rentalsForSelectedUserLoadedListeners = new LinkedList<>();
		selectedUserChangeListeners = new LinkedList<>();
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
		for (SelectedUserChangeListener listener : selectedUserChangeListeners) {
			listener.onSelectedUserChanged(selectedUser);
		}
		if (selectedUser != null) {
			getActiveRentalsForUser();
		}
	}

	public void addRentalsForSelectedUserLoadedListener(RentalsForSelectedUserLoadedListener listener) {
		rentalsForSelectedUserLoadedListeners.add(listener);
	}

	public void addSelectedUserChangeListener(SelectedUserChangeListener listener) {
		selectedUserChangeListeners.add(listener);
	}

	public void bind(MainFrame mainFrame, UserTableModel userTableModel) {
		this.mainFrame = mainFrame;
		this.userTableModel = userTableModel;
		loadEquipmentTypes();
	}

	public void setEquipmentTableModel(EquipmentTableModel equipmentTableModel) {
		this.equipmentTableModel = equipmentTableModel;
	}

	public void setEntriesTableModel(EntriesTableModel entriesTableModel) {
		this.entriesTableModel = entriesTableModel;
	}

	public void setPassesTableModel(PassesTableModel passesTableModel) {
		this.passesTableModel = passesTableModel;
	}

	public void loadUsers() {
		createInvoker("users").get(new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					List<User> users = response.readEntity(new GenericType<List<User>>() {
					});
					userTableModel.setUsers(users);
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

	private void loadEquipmentTypes() {
		createInvoker("equipments/types").get(new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					equipmentTypes = response.readEntity(new GenericType<List<EquipmentType>>() {
					});
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

	public List<EquipmentType> getEquipmentTypes() {
		return equipmentTypes;
	}

	public void loadEquipments() {
		createInvoker("equipments").get(new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					List<Equipment> equipments = response.readEntity(new GenericType<List<Equipment>>() {
					});
					equipmentTableModel.setEquipments(equipments);
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

	public void loadPassesForSelectedUser() {
		Future<Response> responseFuture = createInvoker("passes").get(new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {

				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					List<Pass> passes = response.readEntity(new GenericType<List<Pass>>() {
					});
					passesTableModel.setPasses(passes);
				} else {
					String error = response.readEntity(String.class);
					JOptionPane.showMessageDialog(mainFrame, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void failed(Throwable throwable) {
				throwable.printStackTrace();

			}
		});
	}

	public void addNewUser(User user, final EditUserFrame source) {
		createInvoker("users").post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE),
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
		System.out.println("Buying");
		int validMonths = 0;
		int timeLeft = 0;
		switch (passId) {
			case 1:
				validMonths = 0;
				timeLeft = 0;
				break;
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

		createInvoker("passes/buy").post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
				new InvocationCallback<Response>() {
					@Override
					public void completed(Response response) {
						System.out.println(response);
						if (response.getStatus() == Response.Status.OK.getStatusCode()) {
							source.dispose();
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

	public void rentEquipment(long equipmentId) {
		String path = "rentals/rent/" + selectedUser.getId() + "/" + equipmentId;
		createInvoker(path).post(null, new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					JOptionPane.showMessageDialog(mainFrame, "Successfully rented!", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					loadEquipments();
					getActiveRentalsForUser();
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

	public void returnEquipment(long rentalId) {
		String path = "rentals/return/" + rentalId;
		createInvoker(path).post(null, new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					JOptionPane.showMessageDialog(mainFrame, "Successfully returned!", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					loadEquipments();
					getActiveRentalsForUser();
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

	public void getActiveRentalsForUser() {
		String path = "rentals/active/" + selectedUser.getId();
		callGetService(path, new ResponseProcessor() {
			@Override
			public void processResponse(Response response) {
				List<Rental> rentals = response.readEntity(new GenericType<List<Rental>>() {
				});
				for (RentalsForSelectedUserLoadedListener listener : rentalsForSelectedUserLoadedListeners) {
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
		createInvoker(path).post(entity, new InvocationCallback<Response>() {
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
				.header("Authorization", "magic") //TODO change to token
				.async();
	}

	private interface ResponseProcessor {
		public void processResponse(Response response);
	}
}
