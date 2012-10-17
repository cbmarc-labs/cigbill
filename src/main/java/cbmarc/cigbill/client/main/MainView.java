package cbmarc.cigbill.client.main;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.ImplementedBy;

@ImplementedBy(MainViewImpl.class)
public interface MainView extends IsWidget {
	
	SimplePanel getContentPanel();
	void setBreadcrumb(String[] splitToken);
		
}
