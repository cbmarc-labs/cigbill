package cbmarc.cigbill.client.main.taxes;

import java.util.List;

import cbmarc.cigbill.shared.Tax;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.ListDataProvider;

public class TaxesDatabase {

	private static TaxesDatabase instance;
	private ListDataProvider<Tax> dataProvider = new ListDataProvider<Tax>();

	public TaxesDatabase() {
		generateItems(5);
	}

	public static TaxesDatabase getInstance() {
		if (instance == null) {
			instance = new TaxesDatabase();
		}

		return instance;
	}

	public List<Tax> getList() {
		return dataProvider.getList();
	}

	/**
	 * 
	 */
	public void generateItems(int count) {
		List<Tax> items = dataProvider.getList();

		for (int i = 0; i < count; i++) {
			items.add(createItem(i));
		}
	}

	/**
	 * @param login
	 * @return
	 */
	private Tax createItem(int i) {
		Tax item = new Tax();

		String id = ((Object) item.hashCode()).toString();

		item.setId(id);
		item.setName("Tax " + i);
		item.setDescription("this is a description for a Tax " + i);

		return item;
	}

	private <T> T nextValue(T[] array) {
		return array[Random.nextInt(array.length)];
	}

	public int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

}
