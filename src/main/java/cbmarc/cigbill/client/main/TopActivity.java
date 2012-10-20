package cbmarc.cigbill.client.main;

import javax.inject.Singleton;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.home.HomePlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@Singleton
public class TopActivity extends AbstractActivity implements
		PlaceChangeEvent.Handler {
	
	@Inject
	PlaceController placeController;
	@Inject
	AppConstants appConstants;
	@Inject
	TopView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

		updateAppBreadcrumb();

		eventBus.addHandler(PlaceChangeEvent.TYPE, this);

	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {		
		updateAppBreadcrumb();

	}
	
	// TODO localizated crumbs
	private void updateAppBreadcrumb() {
		// return when not mainplace instance
		if(!(placeController.getWhere() instanceof MainPlace))
			return;
		
		MainPlace mainPlace = ((MainPlace) placeController.getWhere());
		
		String name = mainPlace.getName();
		String[] tokens = mainPlace.getSplitToken();
		
		view.getAppBreadcrumb().clear();
		view.getAppBreadcrumb().addCrumb(appConstants.breadcrumbHome(), "#home:");
		
		if(!(placeController.getWhere() instanceof HomePlace)) {
			view.getAppBreadcrumb().addCrumb(name);
		
			if(!tokens[0].isEmpty())
				view.getAppBreadcrumb().addCrumb(tokens[0]);
		}
	}

}
