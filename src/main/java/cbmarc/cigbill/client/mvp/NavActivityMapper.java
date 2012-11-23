package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.main.NavActivity;
import cbmarc.cigbill.client.main.NavPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class NavActivityMapper implements ActivityMapper {

	@Inject
	private Provider<ActivityAsyncProxy<NavActivity>> navActivity;

	@Override
	public Activity getActivity(Place place) {

		if (place instanceof NavPlace) {
			return navActivity.get();

		}

		return null;

	}
}
