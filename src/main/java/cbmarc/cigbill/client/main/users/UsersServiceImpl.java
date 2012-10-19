package cbmarc.cigbill.client.main.users;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Tax;
import cbmarc.cigbill.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UsersServiceImpl implements UsersServiceAsync {

	private List<User> list = UsersDatabase.getInstance().getList();
	
	public void getAll(final AsyncCallback<List<User>> callback) {
		Collections.sort(list, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getLogin().compareToIgnoreCase(o2.getLogin());
			}
		});

		callback.onSuccess(list);
	}

	@Override
	public void save(User user, final AsyncCallback<Void> callback) {
		if (user.getId() == null) {
			String id = ((Object)user.hashCode()).toString();

			user.setId(id);
			user.setCreated(new Date());

			list.add(user);
		}
		
		user.setUpdated(new Date());

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<User> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void delete(User user, AsyncCallback<Void> callback) {
		this.list.remove(user);
		
		callback.onSuccess(null);
		
	}

	@Override
	public void getById(String id, AsyncCallback<User> callback) {
		User found = null;
		
		for (User user : list) {
			if (user.getId().equals(id)) {
				found = user;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
