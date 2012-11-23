package cbmarc.cigbill.client.main;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(BreadcrumbViewImpl.class)
public interface BreadcrumbView extends IsWidget {
	
	void clear();
	void addCrumb(String text, Place place);
	
	void setPresenter(Presenter presenter);

	public interface Presenter {
		
		void goTo(Place place);
		
	}

}
