package cbmarc.cigbill.client;

import cbmarc.cigbill.client.auth.AuthActivity;
import cbmarc.cigbill.client.auth.AuthView;
import cbmarc.cigbill.client.auth.AuthViewImpl;
import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.i18n.AppMessages;
import cbmarc.cigbill.client.main.MainActivity;
import cbmarc.cigbill.client.main.MainView;
import cbmarc.cigbill.client.main.MainViewImpl;
import cbmarc.cigbill.client.mvp.AppActivity;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ClientFactoryImpl implements ClientFactory {
	
	private AppConstants appConstants = GWT.create(AppConstants.class);
	private AppMessages appMessages = GWT.create(AppMessages.class);
	
	private static final EventBus eventBus = new SimpleEventBus();
	private static final PlaceController placeController = new PlaceController(eventBus);
	
	private AuthView authView = new AuthViewImpl(this);
	private MainView mainView = new MainViewImpl(this);
	
	private AppActivity authActivity;
	private AppActivity mainActivity;
    
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	
	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public AuthView getAuthView() {
		return authView;
	}

	@Override
	public MainView getMainView() {		
		return mainView;
	}

	@Override
	public AppActivity getMainActivity() {
		if(mainActivity == null)
			mainActivity = new MainActivity();
			
		return mainActivity;
	}

	@Override
	public AppActivity getAuthActivity() {
		if(authActivity == null)
			authActivity = new AuthActivity();
			
		return authActivity;
	}

	@Override
	public AppConstants getConstants() {
		return appConstants;
	}

	@Override
	public AppMessages getMessages() {
		return appMessages;
	}

}
