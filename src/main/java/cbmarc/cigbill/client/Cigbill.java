package cbmarc.cigbill.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class Cigbill implements EntryPoint {
	
	AppGinjector ginjector = GWT.create(AppGinjector.class);
	
	public void onModuleLoad() {
		RootPanel.get().add(ginjector.getAppView());
		
		ginjector.getHistoryHandler().handleCurrentHistory();
	}
  
}
