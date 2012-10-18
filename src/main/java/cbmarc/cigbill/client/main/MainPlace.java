package cbmarc.cigbill.client.main;

import com.google.gwt.place.shared.Place;

public abstract class MainPlace extends Place {
	
	protected String token;

	public MainPlace(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
	public String[] getSplitToken() {
		return token.split("/");
	}
	
	public abstract String getName();

}
