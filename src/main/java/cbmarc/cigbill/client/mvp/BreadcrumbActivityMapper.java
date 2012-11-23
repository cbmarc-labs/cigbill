package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.main.BreadcrumbActivity;
import cbmarc.cigbill.client.main.BreadcrumbPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BreadcrumbActivityMapper implements ActivityMapper {

	@Inject
	private Provider<ActivityAsyncProxy<BreadcrumbActivity>> breadcrumbActivity;

	@Override
	public Activity getActivity(Place place) {

		if (place instanceof BreadcrumbPlace) {
			return breadcrumbActivity.get();

		}

		return null;

	}
}
