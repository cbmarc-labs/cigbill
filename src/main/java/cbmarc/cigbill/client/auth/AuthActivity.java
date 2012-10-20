package cbmarc.cigbill.client.auth;

import javax.inject.Inject;
import javax.inject.Singleton;

import cbmarc.cigbill.client.main.home.HomePlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

@Singleton
public class AuthActivity extends AbstractActivity implements
		AuthView.Presenter {

	@Inject
	AuthView view;
	@Inject
	PlaceController placeController;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);

		panel.setWidget(view);
	}

	@Override
	public void doLogin(String login, String password, Boolean remember) {
		placeController.goTo(new HomePlace());

	}

}
