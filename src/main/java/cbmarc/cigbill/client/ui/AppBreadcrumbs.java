package cbmarc.cigbill.client.ui;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;

public class AppBreadcrumbs extends Breadcrumbs {

	private HandlerManager handlerManager = new HandlerManager(this);

	public void addCrumb(String crumb) {
		addCrumb(crumb, null);

	}

	public void addCrumb(String crumb, final Place place) {
		NavLink navLink = new NavLink(crumb);

		if (place != null)
			navLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					handlerManager.fireEvent(new PlaceChangeEvent(place));

				}
			});

		add(navLink);
	}

	public HandlerRegistration addPlaceChangeHandler(
			PlaceChangeEvent.Handler handler) {
		return handlerManager.addHandler(PlaceChangeEvent.TYPE, handler);
	}

}
