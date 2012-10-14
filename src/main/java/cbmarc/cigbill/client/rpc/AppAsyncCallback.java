package cbmarc.cigbill.client.rpc;

import cbmarc.cigbill.client.ui.AppMessage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AppAsyncCallback<T> implements AsyncCallback<T> {
	
	public void onFailure(Throwable caught) {
		
		new AppMessage(caught.getMessage(), AppMessage.ERROR);
		
	}
	
}
