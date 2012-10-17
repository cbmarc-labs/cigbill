package cbmarc.cigbill.client.main.nav;

import javax.inject.Singleton;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@Singleton
public class NavActivity extends AbstractActivity {

	@Inject
	NavView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		
	}

}
