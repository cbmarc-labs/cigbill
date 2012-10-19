package cbmarc.cigbill.client.main.products;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Product;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProductsServiceImpl implements ProductsServiceAsync {

	private List<Product> list = ProductsDatabase.getInstance().getList();

	public void getAll(final AsyncCallback<List<Product>> callback) {
		Collections.sort(list, new Comparator<Product>() {

			@Override
			public int compare(Product o1, Product o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		callback.onSuccess(list);
	}

	@Override
	public void save(Product product, final AsyncCallback<Void> callback) {
		if (product.getId() == null) {
			String id = ((Object) product.hashCode()).toString();

			product.setId(id);

			list.add(product);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Product> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void delete(Product product, AsyncCallback<Void> callback) {
		this.list.remove(product);

		callback.onSuccess(null);

	}

	@Override
	public void getById(String id, AsyncCallback<Product> callback) {
		Product found = null;

		for (Product product : list) {
			if (product.getId().equals(id)) {
				found = product;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
