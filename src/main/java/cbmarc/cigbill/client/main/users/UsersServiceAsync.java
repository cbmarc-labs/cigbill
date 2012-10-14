package cbmarc.cigbill.client.main.users;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UsersServiceAsync {
	
	void getAll(final AsyncCallback<List<User>> callback);
	void getById(String id, final AsyncCallback<User> callback);
	void save(User user, final AsyncCallback<Void> callback);
	void delete(Set<User> list, final AsyncCallback<Void> callback);
	
}
