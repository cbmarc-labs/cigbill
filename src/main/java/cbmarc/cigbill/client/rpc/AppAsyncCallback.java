package cbmarc.cigbill.client.rpc;

import cbmarc.cigbill.client.ui.AppNotify;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AppAsyncCallback<T> implements AsyncCallback<T> {
	
	public void onFailure(Throwable caught) {
		
		AppNotify.error(caught.getMessage());
		
	}
	
}
