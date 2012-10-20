package cbmarc.cigbill.client.main.customers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Customer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CustomersServiceImpl implements CustomersServiceAsync {

	private List<Customer> list = CustomersDatabase.getInstance().getList();

	public void getAll(final AsyncCallback<List<Customer>> callback) {
		Collections.sort(list, new Comparator<Customer>(){

			@Override
			public int compare(Customer o1, Customer o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}});
		
		callback.onSuccess(list);
	}

	@Override
	public void save(Customer customer, final AsyncCallback<Void> callback) {
		if (customer.getId() == null) {
			Long id = Long.parseLong(((Object) customer.hashCode()).toString());

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
	public void delete(Customer customer, AsyncCallback<Void> callback) {
		this.list.remove(customer);

		callback.onSuccess(null);

	}

	@Override
	public void getById(Long id, AsyncCallback<Customer> callback) {
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
