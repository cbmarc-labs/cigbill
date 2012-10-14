package cbmarc.cigbill.client.auth;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;

public interface AuthView extends IsWidget {

	FormPanel getFormPanel();
	TextBox getLogin();
	SubmitButton getSubmitButton();
	
}
