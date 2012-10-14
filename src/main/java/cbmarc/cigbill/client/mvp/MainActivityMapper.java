package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.ClientFactory;
import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.main.customers.CustomersActivity;
import cbmarc.cigbill.client.main.products.ProductsActivity;
import cbmarc.cigbill.client.main.users.UsersActivity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class MainActivityMapper implements ActivityMapper {
	
	private final ClientFactory clientFactory;
	
	public MainActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		if(place instanceof MainPlace) {
			
			AppActivity activity = null;
			String token[] = ((MainPlace) place).getSplitToken();
			
			if (token[0].equalsIgnoreCase("users")) {
				activity = new UsersActivity();
				
			} else if(token[0].equalsIgnoreCase("customers")) {
				activity =  new CustomersActivity();
				
			} else if(token[0].equalsIgnoreCase("products")) {
				activity =  new ProductsActivity();
				
			}
			
			if(activity != null) {
				activity.setClientFactory(clientFactory);
				activity.setPlace(place);
			}
			
			return activity;
		}
			
		return null;
	}

}
