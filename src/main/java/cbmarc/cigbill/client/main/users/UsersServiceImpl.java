package cbmarc.cigbill.client.main.users;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UsersServiceImpl implements UsersServiceAsync {

	private List<User> list = UsersDatabase.getInstance().getList();
	
	public void getAll(final AsyncCallback<List<User>> callback) {
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
