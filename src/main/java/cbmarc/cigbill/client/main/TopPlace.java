package cbmarc.cigbill.client.main;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class TopPlace extends Place {
	
	private static final String NAME = "top";

	@Override
	public boolean equals(Object otherPlace) {
		return this == otherPlace || (otherPlace != null && getClass() == otherPlace.getClass());
	}

	@Prefix(value = NAME)
	public static class Tokenizer implements PlaceTokenizer<TopPlace> {

		@Override
		public TopPlace getPlace(String token) {
			return new TopPlace();
		}
	
		@Override
		public String getToken(TopPlace place) {
			return "";
		}
	}

}
