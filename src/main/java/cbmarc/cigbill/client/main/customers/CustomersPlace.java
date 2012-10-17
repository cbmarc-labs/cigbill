package cbmarc.cigbill.client.main.customers;

import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class CustomersPlace extends MainPlace {

	private static final String TOKEN = "customers";
	
	public CustomersPlace(String token) {
		super(token);
	}
	
	@Prefix(value = TOKEN)
	public static class Tokenizer implements PlaceTokenizer<CustomersPlace> {

		@Override
		public CustomersPlace getPlace(String token) {
			return new CustomersPlace(token);
		}
	
		@Override
		public String getToken(CustomersPlace place) {
			return place.getToken();
		}
	}

}
