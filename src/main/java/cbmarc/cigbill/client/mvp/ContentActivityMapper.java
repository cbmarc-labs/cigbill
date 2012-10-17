package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.auth.AuthActivity;
import cbmarc.cigbill.client.auth.AuthPlace;
import cbmarc.cigbill.client.main.customers.CustomersActivity;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
import cbmarc.cigbill.client.main.users.UsersActivity;
import cbmarc.cigbill.client.main.users.UsersPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ContentActivityMapper implements ActivityMapper {

	@Inject
	private Provider<ActivityAsyncProxy<AuthActivity>> authActivity;
	@Inject
	private Provider<ActivityAsyncProxy<UsersActivity>> usersActivity;
	@Inject
	private Provider<ActivityAsyncProxy<CustomersActivity>> customersActivity;

	@Override
	public Activity getActivity(Place place) {

		if (place instanceof AuthPlace) {
			return authActivity.get();

		} else if (place instanceof UsersPlace) {
			return usersActivity.get();

		} else if (place instanceof CustomersPlace) {
			return customersActivity.get();

		}

		return null;

	}
}
