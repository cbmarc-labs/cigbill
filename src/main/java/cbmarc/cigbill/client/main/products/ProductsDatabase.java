package cbmarc.cigbill.client.main.products;

import java.util.List;

import cbmarc.cigbill.shared.Product;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.ListDataProvider;

public class ProductsDatabase {

	private static final String[] NAME = { "product 1", "product 2",
			"product 3" };
	private static final Float[] PRICE = { 10.10f, 5.00f, 30.44f };

	private static ProductsDatabase instance;
	private ListDataProvider<Product> dataProvider = new ListDataProvider<Product>();

	public ProductsDatabase() {
		generateItems(15);
	}

	public static ProductsDatabase getInstance() {
		if (instance == null) {
			instance = new ProductsDatabase();
		}

		return instance;
	}

	public List<Product> getList() {
		return dataProvider.getList();
	}

	/**
	 * 
	 */
	public void generateItems(int count) {
		List<Product> items = dataProvider.getList();

		for (int i = 0; i < count; i++) {
			items.add(createItem());
		}
	}

	/**
	 * @param login
	 * @return
	 */
	private Product createItem() {
		Product item = new Product();

		String id = ((Object) item.hashCode()).toString();

		item.setId(id);
		item.setName(nextValue(NAME));
		item.setPrice(nextValue(PRICE));

		return item;
	}

	private <T> T nextValue(T[] array) {
		return array[Random.nextInt(array.length)];
	}

	public int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

}
