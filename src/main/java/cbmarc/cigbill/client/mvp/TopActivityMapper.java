package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.main.TopActivity;
import cbmarc.cigbill.client.main.TopPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class TopActivityMapper implements ActivityMapper {

	@Inject
	private Provider<ActivityAsyncProxy<TopActivity>> topActivity;

	@Override
	public Activity getActivity(Place place) {

		if (place instanceof TopPlace) {
			return topActivity.get();

		}

		return null;

	}
}
