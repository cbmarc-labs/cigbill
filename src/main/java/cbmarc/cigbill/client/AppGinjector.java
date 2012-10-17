package cbmarc.cigbill.client;

import cbmarc.cigbill.client.main.MainView;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;

@GinModules({AppGinModule.class})
public interface AppGinjector extends Ginjector {
	
	public EventBus getEventBus();
	public PlaceController getPlaceController();
	public PlaceHistoryHandler getHistoryHandler();
	
	public AppView getAppView();
	public MainView getMainView();

}
