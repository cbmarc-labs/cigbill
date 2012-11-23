package cbmarc.cigbill.client.auth;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

@Singleton
public class AuthViewImpl extends Composite implements AuthView {

	private static AuthViewImplUiBinder uiBinder = GWT
			.create(AuthViewImplUiBinder.class);

	interface AuthViewImplUiBinder extends UiBinder<Widget, AuthViewImpl> {
	}

	@UiField
	FormPanel formPanel;

	@UiField
	TextBox login;

	@UiField
	PasswordTextBox password;

	@UiField
	CheckBox rememberMe;

	@UiField
	SubmitButton submitButton;

	private Presenter presenter;

	public AuthViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		login.setFocus(true);

	}

	@Override
	protected void onAttach() {
		super.onAttach();

		login.setFocus(true);
		login.selectAll();
	}

	@UiHandler("submitButton")
	protected void onClickSubmitButton(ClickEvent event) {
		event.preventDefault();
		formPanel.submit();
	}

	@UiHandler("formPanel")
	protected void onSubmitformPanel(SubmitEvent event) {
		event.cancel();
		presenter.doLogin(login.getValue(), password.getValue(),
				rememberMe.getValue());
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}
