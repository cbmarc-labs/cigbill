package cbmarc.cigbill.client.main.taxes;

import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class TaxesPlace extends MainPlace {

	private static final String NAME = "taxes";
	
	public TaxesPlace() {
		super();
	}
	
	public TaxesPlace(String token) {
		super(token);
	}

	@Prefix(value = NAME)
	public static class Tokenizer implements PlaceTokenizer<TaxesPlace> {

		@Override
		public TaxesPlace getPlace(String token) {
			return new TaxesPlace(token);
		}
	
		@Override
		public String getToken(TaxesPlace place) {
			return place.getToken();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
