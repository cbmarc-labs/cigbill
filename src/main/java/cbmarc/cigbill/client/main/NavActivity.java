package cbmarc.cigbill.client.main;

import javax.inject.Singleton;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@Singleton
public class NavActivity extends AbstractActivity implements NavView.Presenter,
		PlaceChangeEvent.Handler {

	@Inject
	PlaceController placeController;

	@Inject
	NavView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		
		panel.setWidget(view);
		
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
		view.setActive(placeController.getWhere());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		// return when not mainplace instance
		if (!(placeController.getWhere() instanceof MainPlace))
			return;

		view.setActive(placeController.getWhere());

	}

}
