/**
 * 
 */
package cbmarc.cigbill.client.main.home;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author marc
 * 
 */
@Singleton
public class HomeActivity extends AbstractActivity implements
		HomeView.Presenter {

	@Inject
	private HomeView view;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
	}

}
