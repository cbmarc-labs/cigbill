package cbmarc.cigbill.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class Cigbill implements EntryPoint {

	AppGinjector appGinjector = GWT.create(AppGinjector.class);

	public void onModuleLoad() {
		ResourcesInjector.configure();
		
		RootPanel.get().add(appGinjector.getAppView());

		appGinjector.getHistoryHandler().handleCurrentHistory();
	}

}
