package cbmarc.cigbill.client.main.users;

import java.util.Date;
import java.util.List;

import cbmarc.cigbill.shared.User;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.ListDataProvider;

public class UsersDatabase {
	
	private static final String[] LOGIN = { "admin", "user1", "user2" };
	private static final String[] SEX = { "1", "2", "3" };
	private static final Boolean[] ACTIVE = { true, false };
	
	private static UsersDatabase instance;
	private ListDataProvider<User> dataProvider = new ListDataProvider<User>();

	public UsersDatabase() {
		generateItems();
	}

	public static UsersDatabase getInstance() {
		if (instance == null) {
			instance = new UsersDatabase();
		}

		return instance;
	}

	public List<User> getList() {
		return dataProvider.getList();
	}

	/**
	 * 
	 */
	public void generateItems() {
		List<User> items = dataProvider.getList();

		for (String login : LOGIN) {
			items.add(createItem(login));
		}

		// ensure admin login is always active
		((User) items.get(0)).setActive(true);
	}

	/**
	 * @param login
	 * @return
	 */
	private User createItem(String login) {
		User user = new User();

		Long id = Long.parseLong(((Object) user.hashCode()).toString());

		user.setId(id);
		user.setLogin(login);
		user.setPassword(login);
		user.setSex(nextValue(SEX));
		user.setActive(nextValue(ACTIVE));
		user.setCreated(new Date());
		user.setUpdated(new Date());

		return user;
	}

	private <T> T nextValue(T[] array) {
		return array[Random.nextInt(array.length)];
	}

	public int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

}
