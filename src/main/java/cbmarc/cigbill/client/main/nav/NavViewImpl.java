package cbmarc.cigbill.client.main.nav;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class NavViewImpl extends Composite implements NavView {

	private static NavViewImplUiBinder uiBinder = GWT
			.create(NavViewImplUiBinder.class);

	interface NavViewImplUiBinder extends UiBinder<Widget, NavViewImpl> {
	}

	public NavViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
