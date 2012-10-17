package cbmarc.cigbill.client.main.taxes;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Tax;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TaxesServiceImpl implements TaxesServiceAsync {

	private List<Tax> list = TaxesDatabase.getInstance().getList();
	
	public void getAll(final AsyncCallback<List<Tax>> callback) {
		callback.onSuccess(list);
	}

	@Override
	public void save(Tax product, final AsyncCallback<Void> callback) {
		if (product.getId() == null) {
			String id = ((Object)product.hashCode()).toString();

			product.setId(id);

			list.add(product);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Tax> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void getById(String id, AsyncCallback<Tax> callback) {
		Tax found = null;
		
		for (Tax product : list) {
			if (product.getId().equals(id)) {
				found = product;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
