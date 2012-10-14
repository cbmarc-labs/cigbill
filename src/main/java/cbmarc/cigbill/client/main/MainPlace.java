package cbmarc.cigbill.client.main;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MainPlace extends Place {
	
	private String token;
	
	public MainPlace(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
	// Get only the first part of the token
	public String[] getSplitToken() {
		return token.split("/");
	}

	@Prefix("main")
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
