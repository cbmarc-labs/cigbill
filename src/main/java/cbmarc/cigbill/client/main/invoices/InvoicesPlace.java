package cbmarc.cigbill.client.main.invoices;

import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class InvoicesPlace extends MainPlace {

	private static final String NAME = "invoices";
	
	public InvoicesPlace(String token) {
		super(token);
	}

	@Prefix(value = NAME)
	public static class Tokenizer implements PlaceTokenizer<InvoicesPlace> {

		@Override
		public InvoicesPlace getPlace(String token) {
			return new InvoicesPlace(token);
		}
	
		@Override
		public String getToken(InvoicesPlace place) {
			return place.getToken();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
