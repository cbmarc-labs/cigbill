package cbmarc.cigbill.client.auth;

import cbmarc.cigbill.client.ClientFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AuthViewImpl extends Composite implements AuthView {

	private static AuthViewImplUiBinder uiBinder = GWT
			.create(AuthViewImplUiBinder.class);

	interface AuthViewImplUiBinder extends UiBinder<Widget, AuthViewImpl> {}
	
	@UiField FormPanel formPanel;
	@UiField TextBox login;
	@UiField PasswordTextBox password;
	@UiField CheckBox remember;
	@UiField SubmitButton submitButton;

	public AuthViewImpl(ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));
		
		
	}

	@Override
	public FormPanel getFormPanel() {
		return formPanel;
	}

	@Override
	public SubmitButton getSubmitButton() {
		return submitButton;
	}

	@Override
	public TextBox getLogin() {
		return login;
	}

}
