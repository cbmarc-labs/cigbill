package cbmarc.cigbill.client.main.customers;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.main.users.UsersDatabase;
import cbmarc.cigbill.shared.Customer;
import cbmarc.cigbill.shared.User;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CustomersServiceImpl implements CustomersServiceAsync {
	
	private List<Customer> list = CustomersDatabase.getInstance().getList();

	public void getAll(final AsyncCallback<List<Customer>> callback) {
		callback.onSuccess(list);
	}

	@Override
	public void save(Customer customer, final AsyncCallback<Void> callback) {
		if (customer.getId() == null) {
			String id = ((Object)customer.hashCode()).toString();
			
			customer.setId(id);
			
			list.add(customer);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Customer> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void getById(String id, AsyncCallback<Customer> callback) {		
		Customer found = null;
		
		for (Customer customer : list) {
			if (customer.getId().equals(id)) {
				found = customer;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
