package cbmarc.cigbill.client;

import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.mvp.AppActivityMapper;
import cbmarc.cigbill.client.mvp.AppPlaceHistoryMapper;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;

public class Cigbill implements EntryPoint {
	
	private Place defaultPlace = new MainPlace("users/add");
	private RootViewImpl rootView = new RootViewImpl();
	
	public void onModuleLoad() {
		ClientFactory clientFactory = GWT.create(ClientFactory.class);
	
		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();
		
		ActivityMapper appActivityMapper = new AppActivityMapper(clientFactory);
		ActivityManager appActivityManager = new ActivityManager(appActivityMapper, eventBus);
		appActivityManager.setDisplay(rootView);
		
		AppPlaceHistoryMapper historyMapper= GWT.create(AppPlaceHistoryMapper.class);
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);
		
		RootPanel.get().add(rootView);
		historyHandler.handleCurrentHistory();
	}
  
}
