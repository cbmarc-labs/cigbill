package cbmarc.cigbill.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppNav extends Composite {

	private static AppNavUiBinder uiBinder = GWT.create(AppNavUiBinder.class);

	interface AppNavUiBinder extends UiBinder<Widget, AppNav> {
	}

	public AppNav() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
