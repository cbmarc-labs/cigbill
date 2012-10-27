package cbmarc.cigbill.client.main;

import cbmarc.cigbill.client.ui.AppBreadcrumbs;
import cbmarc.cigbill.client.ui.AppNav;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class TopViewImpl extends Composite implements TopView,
		PlaceChangeEvent.Handler {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, TopViewImpl> {
	}

	private Presenter presenter;

	@UiField
	AppNav appNav;

	@UiField
	AppBreadcrumbs appBreadcrumbs;

	public TopViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		appNav.addPlaceChangeHandler(this);
		appBreadcrumbs.addPlaceChangeHandler(this);
	}

	@Override
	public AppBreadcrumbs getAppBreadcrumbs() {
		return appBreadcrumbs;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		presenter.goTo(event.getNewPlace());

	}

}
