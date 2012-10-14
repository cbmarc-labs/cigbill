package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.ClientFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

public interface AppActivity extends Activity {
	
	public void setClientFactory(ClientFactory clientFactory);

	public void setPlace(Place place);

}
