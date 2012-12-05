package cbmarc.cigbill.client.main.items;

import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ItemsPlace extends MainPlace {

	private static final String NAME = "items";
	
	public ItemsPlace() {
		super();
	}
	
	public ItemsPlace(String token) {
		super(token);
	}

	@Prefix(value = NAME)
	public static class Tokenizer implements PlaceTokenizer<ItemsPlace> {

		@Override
		public ItemsPlace getPlace(String token) {
			return new ItemsPlace(token);
		}
	
		@Override
		public String getToken(ItemsPlace place) {
			return place.getToken();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
