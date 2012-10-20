package cbmarc.cigbill.client.main.invoices;

import java.util.List;

import cbmarc.cigbill.shared.Invoice;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.ListDataProvider;

public class InvoicesDatabase {

	/*private static final String[] ARTICLES = { "a ", "the ", "one ", "some ",
			"any " };*/

	private static InvoicesDatabase instance;
	private ListDataProvider<Invoice> dataProvider = new ListDataProvider<Invoice>();

	public InvoicesDatabase() {
		generateItems(5);
	}

	public static InvoicesDatabase getInstance() {
		if (instance == null) {
			instance = new InvoicesDatabase();
		}

		return instance;
	}

	public List<Invoice> getList() {
		return dataProvider.getList();
	}

	/**
	 * 
	 */
	public void generateItems(int count) {
		List<Invoice> items = dataProvider.getList();

		for (int i = 0; i < count; i++) {
			items.add(createItem(i));
		}
	}

	/**
	 * @param login
	 * @return
	 */
	private Invoice createItem(int i) {
		Invoice item = new Invoice();

		Long id = Long.parseLong(((Object) item.hashCode()).toString());

		item.setId(id);

		return item;
	}

	private <T> T nextValue(T[] array) {
		return array[Random.nextInt(array.length)];
	}

	public int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

}
