package hu.bme.vik.szoftarch.climbingorgmanager.client.controller;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hu.bme.vik.szoftarch.climbingorgmanager.client.util.GsonMessageBodyHandler;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Token;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.20..
 */
public class Controller {

	private static Controller instance;

	private Token token;

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	private Controller() {

	}

	public void loadUsers(final ServiceCallback<List<User>> serviceCallback) {
		Client client = ClientBuilder.newClient().register(GsonMessageBodyHandler.class);
		client.target("http://climbingorgmanager-asztalosdani.rhcloud.com/rest-1.0-SNAPSHOT/rest")
				.path("users").request(MediaType.APPLICATION_JSON_TYPE)
				.header("Authorization", "magic") //TODO change to token
				.async().get(new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				System.out.println(response);
				List<User> users = response.readEntity(new GenericType<List<User>>() {
				});
				serviceCallback.onCompleted(users);
			}

			@Override
			public void failed(Throwable throwable) {
				serviceCallback.onFailed(throwable.toString());
			}
		});
	}

	public interface ServiceCallback<T> {
		public void onCompleted(T t);

		public void onFailed(String errorMessage);
	}
}
