package cbmarc.cigbill.client.main.products;

import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ProductsPlace extends MainPlace {

	private static final String NAME = "products";
	
	public ProductsPlace(String token) {
		super(token);
	}

	@Prefix(value = NAME)
	public static class Tokenizer implements PlaceTokenizer<ProductsPlace> {

		@Override
		public ProductsPlace getPlace(String token) {
			return new ProductsPlace(token);
		}
	
		@Override
		public String getToken(ProductsPlace place) {
			return place.getToken();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
