package cbmarc.cigbill.client.main;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MainPlace extends Place {
	
	private static final String TOKEN = "main";
	
	protected String token;

	public MainPlace(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	@Prefix(value = TOKEN)
	public static class Tokenizer implements PlaceTokenizer<MainPlace> {

		@Override
		public MainPlace getPlace(String token) {
			return new MainPlace(token);
		}
	
		@Override
		public String getToken(MainPlace place) {
			return place.getToken();
		}
	}

}
