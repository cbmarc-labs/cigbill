package cbmarc.cigbill.client.main;

import cbmarc.cigbill.client.ui.AppBreadcrumb;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(TopViewImpl.class)
public interface TopView extends IsWidget {
	
	AppBreadcrumb getAppBreadcrumb();
		
}
