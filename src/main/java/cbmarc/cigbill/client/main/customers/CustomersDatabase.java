package cbmarc.cigbill.client.main.customers;

import java.util.List;

import cbmarc.cigbill.shared.Customer;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.ListDataProvider;

public class CustomersDatabase {

	private static final String[] FEMALE_FIRST_NAMES = { "Mary", "Patricia",
			"Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan",
			"Margaret", "Dorothy", "Lisa", "Nancy", "Karen", "Betty", "Helen",
			"Sandra", "Donna", "Carol", "Ruth", "Sharon", "Michelle", "Laura",
			"Sarah", "Kimberly", "Deborah", "Jessica", "Shirley", "Cynthia",
			"Angela", "Melissa", "Brenda", "Amy", "Anna", "Rebecca", "Alma",
			"Sue", "Elsie", "Beth", "Jeanne" };
	private static final String[] MALE_FIRST_NAMES = { "James", "John",
			"Robert", "Michael", "William", "David", "Richard", "Charles",
			"Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark",
			"Donald", "George", "Kenneth", "Steven", "Edward", "Brian",
			"Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary",
			"Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric",
			"Lloyd", "Tommy", "Leon", "Derek", "Warren", "Darrell", "Jerome",
			"Floyd", "Leo", "Alvin", "Tim", "Wesley", "Gordon", "Dean", "Greg",
			"Jorge", "Dustin", "Pedro", "Derrick", "Dan", "Lewis", "Zachary",
			"Corey", "Herman", "Maurice", "Vernon", "Roberto", "Clyde", "Glen",
			"Hector", "Shane", "Ricardo", "Sam", "Rick", "Lester", "Brent",
			"Ramon", "Charlie", "Tyler", "Gilbert", "Gene" };
	private static final String[] LAST_NAMES = { "Smith", "Johnson",
			"Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
			"Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris",
			"Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark",
			"Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young",
			"Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green",
			"Duncan", "Snyder", "Hart", "Cunningham", "Bradley", "Lane",
			"Andrews", "Ruiz", "Harper", "Fox", "Riley", "Armstrong",
			"Carpenter", "Weaver", "Greene", "Lawrence", "Elliott", "Chavez",
			"Sims", "Austin", "Peters", "Kelley", "Franklin", "Lawson" };

	private static CustomersDatabase instance;
	private ListDataProvider<Customer> dataProvider = new ListDataProvider<Customer>();

	public CustomersDatabase() {
		generateItems(5);
	}

	public static CustomersDatabase getInstance() {
		if (instance == null) {
			instance = new CustomersDatabase();
		}

		return instance;
	}

	public List<Customer> getList() {
		return dataProvider.getList();
	}

	public void generateItems(int count) {
		List<Customer> items = dataProvider.getList();

		for (int i = 0; i < count; i++) {
			items.add(createItem());
		}
	}

	private Customer createItem() {
		Customer item = new Customer();

		String firstName;
		String lastName = nextValue(LAST_NAMES);

		item.setId(((Object) item.hashCode()).toString());
		if (Random.nextBoolean()) {
			firstName = nextValue(MALE_FIRST_NAMES);

		} else {
			firstName = nextValue(FEMALE_FIRST_NAMES);
		}

		item.setName(firstName + " " + lastName);
		item.setEmail(lastName.toLowerCase() + "@" + firstName.toLowerCase()
				+ ".com");

		return item;
	}

	private <T> T nextValue(T[] array) {
		return array[Random.nextInt(array.length)];
	}

	public int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

}
