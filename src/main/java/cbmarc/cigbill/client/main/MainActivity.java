package cbmarc.cigbill.client.main;

import cbmarc.cigbill.client.mvp.AppAbstractActivity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MainActivity extends AppAbstractActivity implements
		PlaceChangeEvent.Handler {

	MainView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getMainView();

		panel.setWidget(view);

		view.setBreadcrumb(((MainPlace) place).getSplitToken());
		
		clientFactory.getEventBus().addHandler(PlaceChangeEvent.TYPE, this);
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		view.setBreadcrumb(((MainPlace) place).getSplitToken());

	}

}
