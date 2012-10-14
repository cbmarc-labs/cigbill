package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.ClientFactory;
import cbmarc.cigbill.client.auth.AuthPlace;
import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {
	
	private final ClientFactory clientFactory;
	
	public AppActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		AppActivity activity = null;
		
		if(place instanceof AuthPlace)
			activity = clientFactory.getAuthActivity();
		
		else if(place instanceof MainPlace)
			activity = clientFactory.getMainActivity();
				
		if(activity != null) {
			activity.setClientFactory(clientFactory);
			activity.setPlace(place);
		}
		
		return activity;
	}
}
