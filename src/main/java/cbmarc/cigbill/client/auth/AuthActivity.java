package cbmarc.cigbill.client.auth;

import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.mvp.AppAbstractActivity;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class AuthActivity extends AppAbstractActivity {
	
	private AuthView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getAuthView();
		
		panel.setWidget(view);
		
		view.getLogin().setFocus(true);
		view.getLogin().selectAll();
		
		bind();
	}
	
	private void bind() {
		// Hack for cancel form submit
		view.getSubmitButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				view.getFormPanel().submit();
				
			}});
		
		view.getFormPanel().addSubmitHandler(new SubmitHandler(){

			@Override
			public void onSubmit(SubmitEvent event) {
				event.cancel();
				
				clientFactory.getPlaceController().goTo(new MainPlace(""));
				
			}});
	}

}
