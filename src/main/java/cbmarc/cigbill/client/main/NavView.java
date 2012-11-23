package cbmarc.cigbill.client.main;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(NavViewImpl.class)
public interface NavView extends IsWidget {
	
	void setActive(Place place);
	
	void setPresenter(Presenter presenter);

	public interface Presenter {
		
		void goTo(Place place);
		
	}

}
