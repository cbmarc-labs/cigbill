package cbmarc.cigbill.client.auth;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class AuthPlace extends Place {
	
	private String token;
	
	public AuthPlace(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	@Prefix("auth")
	public static class Tokenizer implements PlaceTokenizer<AuthPlace> {

		@Override
		public AuthPlace getPlace(String token) {
			return new AuthPlace(token);
		}
	
		@Override
		public String getToken(AuthPlace place) {
			return place.getToken();
		}
	}

}
