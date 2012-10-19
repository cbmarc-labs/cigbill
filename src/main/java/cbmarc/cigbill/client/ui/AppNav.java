package cbmarc.cigbill.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppNav extends Composite {

	private static AppNavUiBinder uiBinder = GWT.create(AppNavUiBinder.class);

	interface AppNavUiBinder extends UiBinder<Widget, AppNav> {
	}

	@UiField
	Anchor moneyAnchor, stockAnchor, peopleAnchor, settingsAnchor;

	public AppNav() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	// IE8 bug
	@UiHandler(value = { "moneyAnchor", "stockAnchor", "peopleAnchor",
			"settingsAnchor" })
	protected void onClickValidation(ClickEvent event) {
		event.preventDefault();
	}

}
