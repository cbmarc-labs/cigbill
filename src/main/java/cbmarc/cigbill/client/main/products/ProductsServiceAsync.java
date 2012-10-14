package cbmarc.cigbill.client.main.products;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Product;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProductsServiceAsync {
	
	void getAll(final AsyncCallback<List<Product>> callback);
	void getById(String id, final AsyncCallback<Product> callback);
	void save(Product product, final AsyncCallback<Void> callback);
	void delete(Set<Product> list, final AsyncCallback<Void> callback);
	
}
