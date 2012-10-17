package cbmarc.cigbill.client.main.payments;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Payment;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class PaymentsServiceImpl implements PaymentsServiceAsync {

	private List<Payment> list = PaymentsDatabase.getInstance().getList();

	@Override
	public void getAll(final AsyncCallback<List<Payment>> callback) {
		callback.onSuccess(list);
	}

	@Override
	public void save(Payment payment, final AsyncCallback<Void> callback) {
		if (payment.getId() == null) {
			String id = ((Object) payment.hashCode()).toString();

			payment.setId(id);

			list.add(payment);
		} else {
			Payment p = find(payment.getId());
			p.copy(payment);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Payment> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void getById(String id, AsyncCallback<Payment> callback) {
		Payment found = find(id);
		
		Payment payment = null;
		if(find(id) != null)
			payment = found.clone();
		
		callback.onSuccess(payment);
	}

	private Payment find(String id) {
		Payment found = null;

		for (Payment item : list) {
			if (item.getId().equals(id)) {
				return item;
			}
		}

		return found;
	}

}
