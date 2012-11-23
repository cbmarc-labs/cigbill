package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.main.NavPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.CachingActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

public class CachingNavActivityMapper implements ActivityMapper {
	
	private ActivityMapper filteredActivityMapper;
	
	@Inject
	public CachingNavActivityMapper(NavActivityMapper mapper) {
		FilteredActivityMapper.Filter filter = new FilteredActivityMapper.Filter() {

			@Override
			public Place filter(Place place) {
				return place instanceof MainPlace ? new NavPlace() : place;
			}};
		
		CachingActivityMapper cachingActivityMapper = new CachingActivityMapper(mapper);
	    filteredActivityMapper = new FilteredActivityMapper(filter, cachingActivityMapper);
	}

	@Override
	public Activity getActivity(Place place) {
		return filteredActivityMapper.getActivity(place);
	}

}
