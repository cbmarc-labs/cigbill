package cbmarc.cigbill.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.RootPanel;

public class Cigbill implements EntryPoint {

	AppGinjector ginjector = GWT.create(AppGinjector.class);

	public void onModuleLoad() {
		// GWT CssResource does not support @media
		//AppResources.INSTANCE.css().ensureInjected();
		StyleInjector.inject(AppResources.INSTANCE.css().getText());

		RootPanel.get().add(ginjector.getAppView());

		ginjector.getHistoryHandler().handleCurrentHistory();
	}

}
