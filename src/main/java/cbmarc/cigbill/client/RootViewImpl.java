package cbmarc.cigbill.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class RootViewImpl extends Composite implements RootView, AcceptsOneWidget {

	private static RootViewImplUiBinder uiBinder = GWT
			.create(RootViewImplUiBinder.class);

	interface RootViewImplUiBinder extends UiBinder<Widget, RootViewImpl> {}
	
	@UiField SimplePanel contentPanel;

	public RootViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setWidget(IsWidget w) {
		contentPanel.clear();
		contentPanel.add(w);
		
	}

}
