package cbmarc.cigbill.client.main;

import com.google.gwt.user.client.ui.IsWidget;

public interface MainView extends IsWidget {
	
	void setBreadcrumb(String[] splitToken);
		
}
