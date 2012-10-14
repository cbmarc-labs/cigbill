package cbmarc.cigbill.client;

import cbmarc.cigbill.client.auth.AuthView;
import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.i18n.AppMessages;
import cbmarc.cigbill.client.main.MainView;
import cbmarc.cigbill.client.mvp.AppActivity;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {
	EventBus getEventBus();
    PlaceController getPlaceController();
    
    AuthView getAuthView();
    MainView getMainView();
    
    AppActivity getAuthActivity();
    AppActivity getMainActivity();
    
    AppConstants getConstants();
    AppMessages getMessages();
}
