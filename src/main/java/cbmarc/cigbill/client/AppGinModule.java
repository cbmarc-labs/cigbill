package cbmarc.cigbill.client;

import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.mvp.AppPlaceHistoryMapper;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AppGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class).in(Singleton.class);
		
	}
	
	@Provides 
	@Singleton  
	public PlaceHistoryHandler getHistoryHandler(PlaceController placeController,
			PlaceHistoryMapper historyMapper, EventBus eventBus) {
		PlaceHistoryHandler historyHandler =  new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, new MainPlace(""));
		
		return historyHandler;
	}
	
	@Provides
	@Singleton
	public PlaceController getPlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}

}
