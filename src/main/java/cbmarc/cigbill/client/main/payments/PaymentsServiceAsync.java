package cbmarc.cigbill.client.main.payments;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Payment;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaymentsServiceAsync {

	void getAll(final AsyncCallback<List<Payment>> callback);

	void getById(String id, final AsyncCallback<Payment> callback);

	void save(Payment product, final AsyncCallback<Void> callback);

	void delete(Set<Payment> list, final AsyncCallback<Void> callback);

	void delete(Payment payment, final AsyncCallback<Void> callback);

}
