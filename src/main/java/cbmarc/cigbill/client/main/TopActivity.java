package cbmarc.cigbill.client.main;

import javax.inject.Singleton;

import cbmarc.cigbill.client.i18n.AppConstants;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@Singleton
public class TopActivity extends AbstractActivity implements TopView.Presenter {

	@Inject
	PlaceController placeController;

	@Inject
	AppConstants appConstants;

	@Inject
	TopView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		
		view.setPresenter(this);

	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

}
