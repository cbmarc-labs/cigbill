package cbmarc.cigbill.client.main;

import cbmarc.cigbill.client.ui.AppBreadcrumb;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class TopViewImpl extends Composite implements TopView {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, TopViewImpl> {
	}

	@UiField
	AppBreadcrumb appBreadcrumb;

	public TopViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public AppBreadcrumb getAppBreadcrumb() {
		return appBreadcrumb;
	}

}
