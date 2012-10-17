package cbmarc.cigbill.client.main.nav;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class NavPlace extends Place {
	
	private static final String TOKEN = "nav";

  /**
   * equality test based on Class type, to let different instance of this
   * Place class to be equals for CachingActivityMapper test on Place equality
   *
   * @param otherPlace the place to compare with
   * @return true if this place and otherPlace are of the same Class type
   */
  @Override
  public boolean equals(Object otherPlace) {//Window.alert(otherPlace.toString());
    return this == otherPlace || (otherPlace != null && getClass() == otherPlace.getClass());
  }

	@Prefix(value = TOKEN)
	public static class Tokenizer implements PlaceTokenizer<NavPlace> {

		@Override
		public NavPlace getPlace(String token) {
			return new NavPlace();
		}
	
		@Override
		public String getToken(NavPlace place) {
			return "";
		}
	}

}
