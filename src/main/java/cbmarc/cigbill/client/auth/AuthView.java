package cbmarc.cigbill.client.auth;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(AuthViewImpl.class)
public interface AuthView extends IsWidget {
	
	public void setPresenter(Presenter presenter);

	//@ImplementedBy(AuthActivity.class)
	public interface Presenter {
		public void doLogin(String login, String password, Boolean remember);

	}

}
