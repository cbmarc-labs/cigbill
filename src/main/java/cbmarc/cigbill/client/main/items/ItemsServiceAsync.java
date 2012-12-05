package cbmarc.cigbill.client.main.items;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Item;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ItemsServiceAsync {

	void getAll(final AsyncCallback<List<Item>> callback);

	void getById(Long id, final AsyncCallback<Item> callback);

	void save(Item item, final AsyncCallback<Void> callback);

	void delete(Set<Item> list, final AsyncCallback<Void> callback);

	void delete(Item item, final AsyncCallback<Void> callback);

}
