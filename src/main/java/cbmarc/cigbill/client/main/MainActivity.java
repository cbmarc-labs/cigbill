package cbmarc.cigbill.client.main;

import javax.inject.Singleton;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@Singleton
public class MainActivity extends AbstractActivity implements
		PlaceChangeEvent.Handler {

	@Inject
	MainView view;
	@Inject
	PlaceController placeController;
	
	//@Inject
	//PlaceHistoryHandler mainPlaceHistoryHandler;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

		//view.setBreadcrumb(((MainPlace) placeController.getWhere())
		//		.getSplitToken());
		
		//mainPlaceHistoryHandler.handleCurrentHistory();

		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		// view.setBreadcrumb(((MainPlace) place).getSplitToken());

	}

}
