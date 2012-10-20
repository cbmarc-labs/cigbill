package cbmarc.cigbill.client.main.payments;

import java.util.List;

import cbmarc.cigbill.shared.Payment;

import com.google.gwt.view.client.ListDataProvider;

public class PaymentsDatabase {

	private static final String[] NAME = { "Cash", "Credit Card" };

	private static PaymentsDatabase instance;
	private ListDataProvider<Payment> dataProvider = new ListDataProvider<Payment>();

	public PaymentsDatabase() {
		generateItems();
	}

	public static PaymentsDatabase getInstance() {
		if (instance == null) {
			instance = new PaymentsDatabase();
		}

		return instance;
	}

	public List<Payment> getList() {
		return dataProvider.getList();
	}

	/**
	 * 
	 */
	public void generateItems() {
		List<Payment> items = dataProvider.getList();

		for (String name : NAME) {
			items.add(createItem(name));
		}
	}

	/**
	 * @param login
	 * @return
	 */
	private Payment createItem(String name) {
		Payment item = new Payment();

		Long id = Long.parseLong(((Object) item.hashCode()).toString());

		item.setId(id);
		item.setName(name);

		return item;
	}

	public int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

}
