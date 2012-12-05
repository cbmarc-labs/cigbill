package cbmarc.cigbill.client.main.items;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Item;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ItemsServiceImpl implements ItemsServiceAsync {

	private List<Item> list = ItemsDatabase.getInstance().getList();

	public void getAll(final AsyncCallback<List<Item>> callback) {
		Collections.sort(list, new Comparator<Item>() {

			@Override
			public int compare(Item o1, Item o2) {
				return o1.getDescription().compareToIgnoreCase(o2.getDescription());
			}
		});

		callback.onSuccess(list);
	}

	@Override
	public void save(Item item, final AsyncCallback<Void> callback) {
		if (item.getId() == null) {
			Long id = Long.parseLong(((Object) item.hashCode()).toString());

			item.setId(id);

			list.add(item);
		}

		callback.onSuccess(null);
	}

	@Override
	public void delete(Set<Item> list, AsyncCallback<Void> callback) {
		this.list.removeAll(list);

		callback.onSuccess(null);

	}

	@Override
	public void delete(Item item, AsyncCallback<Void> callback) {
		this.list.remove(item);

		callback.onSuccess(null);

	}

	@Override
	public void getById(Long id, AsyncCallback<Item> callback) {
		Item found = null;

		for (Item item : list) {
			if (item.getId().equals(id)) {
				found = item;
				break;
			}
		}

		callback.onSuccess(found);
	}

}
