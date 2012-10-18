package cbmarc.cigbill.client.main.users;

import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class UsersPlace extends MainPlace {
	
	private static final String NAME = "users";
	
	public UsersPlace(String token) {
		super(token);
	}

	@Prefix(value = NAME)
	public static class Tokenizer implements PlaceTokenizer<UsersPlace> {

		@Override
		public UsersPlace getPlace(String token) {
			return new UsersPlace(token);
		}
	
		@Override
		public String getToken(UsersPlace place) {
			return place.getToken();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
