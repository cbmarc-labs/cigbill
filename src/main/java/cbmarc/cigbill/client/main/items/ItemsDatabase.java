package cbmarc.cigbill.client.main.items;

import java.util.List;

import cbmarc.cigbill.shared.Item;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.ListDataProvider;

public class ItemsDatabase {

	private static ItemsDatabase instance;
	private ListDataProvider<Item> dataProvider = new ListDataProvider<Item>();

	public ItemsDatabase() {
		generateItems(15);
	}

	public static ItemsDatabase getInstance() {
		if (instance == null) {
			instance = new ItemsDatabase();
		}

		return instance;
	}

	public List<Item> getList() {
		return dataProvider.getList();
	}

	/**
	 * 
	 */
	public void generateItems(int count) {
		List<Item> items = dataProvider.getList();

		for (int i = 0; i < count; i++) {
			items.add(createItem());
		}
	}

	/**
	 * @param login
	 * @return
	 */
	private Item createItem() {
		Item item = new Item();

		Long id = Long.parseLong(((Object) item.hashCode()).toString());

		item.setId(id);
		item.setDescription("Description of item " + id);
		item.setQuantity(Random.nextDouble() * 5);
		item.setPrice(Double.valueOf(NumberFormat.getFormat("#.00").format(
				Random.nextDouble() * 2000)));
		item.setNotes("Notes of item " + id);

		return item;
	}

	private <T> T nextValue(T[] array) {
		return array[Random.nextInt(array.length)];
	}

	public int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

}
