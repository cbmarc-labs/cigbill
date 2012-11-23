package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.auth.AuthActivity;
import cbmarc.cigbill.client.auth.AuthPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AuthActivityMapper implements ActivityMapper {

	@Inject
	private Provider<ActivityAsyncProxy<AuthActivity>> authActivity;

	@Override
	public Activity getActivity(Place place) {

		if (place instanceof AuthPlace) {
			return authActivity.get();

		}

		return null;

	}
}
