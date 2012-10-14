package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.ClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;

public abstract class AppAbstractActivity extends AbstractActivity implements AppActivity {

	protected ClientFactory clientFactory;
	protected Place place;

	@Override
	public void setClientFactory(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		
	}

	@Override
	public void setPlace(Place place) {
		this.place = place;
		
	}

}
