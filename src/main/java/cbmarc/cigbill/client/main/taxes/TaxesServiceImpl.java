package cbmarc.cigbill.client.main.taxes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Tax;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TaxesServiceImpl implements TaxesServiceAsync {

	private List<Tax> list = TaxesDatabase.getInstance().getList();

	public void getAll(final AsyncCallback<List<Tax>> callback) {
		Collections.sort(list, new Comparator<Tax>() {

			@Override
			public int compare(Tax o1, Tax o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		callback.onSuccess(list);
	}

	@Override
	public void save(Tax tax, final AsyncCallback<Void> callback) {
		if (tax.getId() == null) {
			Long id = Long.parseLong(((Object) tax.hashCode()).toString());

			tax.setId(id);

			list.add(tax);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Tax> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void delete(Tax tax, AsyncCallback<Void> callback) {
		this.list.remove(tax);

		callback.onSuccess(null);

	}

	@Override
	public void getById(Long id, AsyncCallback<Tax> callback) {
		Tax found = null;

		for (Tax tax : list) {
			if (tax.getId().equals(id)) {
				found = tax;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
