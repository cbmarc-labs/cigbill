package cbmarc.cigbill.client.main.invoices;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Invoice;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class InvoicesServiceImpl implements InvoicesServiceAsync {

	private List<Invoice> list = InvoicesDatabase.getInstance().getList();

	public void getAll(final AsyncCallback<List<Invoice>> callback) {
		Collections.sort(list, new Comparator<Invoice>() {

			@Override
			public int compare(Invoice o2, Invoice o1) {
				return o1.getId().compareTo(o2.getId());
			}
		});

		callback.onSuccess(list);
	}

	@Override
	public void save(Invoice product, final AsyncCallback<Void> callback) {
		if (product.getId() == null) {
			Long id = Long.valueOf(((Object) product.hashCode()).toString());

			product.setId(id);

			list.add(product);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Invoice> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void delete(Invoice invoice, AsyncCallback<Void> callback) {
		this.list.remove(invoice);

		callback.onSuccess(null);

	}

	@Override
	public void getById(Long id, AsyncCallback<Invoice> callback) {
		Invoice found = null;

		for (Invoice product : list) {
			if (product.getId().equals(id)) {
				found = product;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
