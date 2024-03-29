package cbmarc.cigbill.client.main.products;

import java.util.List;

import cbmarc.cigbill.shared.Product;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.ListDataProvider;

public class ProductsDatabase {

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

		Long id = Long.parseLong(((Object) item.hashCode()).toString());

		item.setId(id);
		item.setName("Product " + id);
		item.setDescription("Description of item " + id);
		item.setPrice(Double.valueOf(NumberFormat.getFormat("#.00").format(
				Random.nextDouble() * 10000)));
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
