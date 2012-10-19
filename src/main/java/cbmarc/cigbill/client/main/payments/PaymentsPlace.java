package cbmarc.cigbill.client.main.payments;

import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class PaymentsPlace extends MainPlace {

	private static final String NAME = "payments";
	
	public PaymentsPlace() {
		super();
	}
	
	public PaymentsPlace(String token) {
		super(token);
	}

	@Prefix(value = NAME)
	public static class Tokenizer implements PlaceTokenizer<PaymentsPlace> {

		@Override
		public PaymentsPlace getPlace(String token) {
			return new PaymentsPlace(token);
		}
	
		@Override
		public String getToken(PaymentsPlace place) {
			return place.getToken();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
