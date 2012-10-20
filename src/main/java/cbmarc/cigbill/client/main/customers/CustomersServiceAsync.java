package cbmarc.cigbill.client.main.customers;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Customer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CustomersServiceAsync {

	void getAll(final AsyncCallback<List<Customer>> callback);

	void getById(Long id, final AsyncCallback<Customer> callback);

	void save(Customer customer, final AsyncCallback<Void> callback);

	void delete(Set<Customer> list, final AsyncCallback<Void> callback);

	void delete(Customer customer, final AsyncCallback<Void> callback);

}
