package cbmarc.cigbill.client.main.payments;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Payment;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class PaymentsServiceImpl implements PaymentsServiceAsync {

	private List<Payment> list = PaymentsDatabase.getInstance().getList();

	@Override
	public void getAll(final AsyncCallback<List<Payment>> callback) {
		Collections.sort(list, new Comparator<Payment>() {

			@Override
			public int compare(Payment o1, Payment o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		callback.onSuccess(list);
	}

	@Override
	public void save(Payment payment, final AsyncCallback<Void> callback) {
		if (payment.getId() == null) {
			Long id = Long.parseLong(((Object) payment.hashCode()).toString());

			payment.setId(id);

			list.add(payment);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Payment> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void delete(Payment payment, AsyncCallback<Void> callback) {
		this.list.remove(payment);

		callback.onSuccess(null);

	}

	@Override
	public void getById(Long id, AsyncCallback<Payment> callback) {
		Payment found = null;

		for (Payment payment : list) {
			if (payment.getId().equals(id)) {
				found = payment;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
