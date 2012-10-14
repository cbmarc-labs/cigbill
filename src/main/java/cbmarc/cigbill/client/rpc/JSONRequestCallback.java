package cbmarc.cigbill.client.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public abstract class JSONRequestCallback implements RequestCallback {

	@Override
	public void onResponseReceived(Request request, Response response) {
		if (200 == response.getStatusCode()) {
				onSuccess(request, response.getText());
		
		} else if (404 == response.getStatusCode()) {
			// 404, can connect server the server, but can locate the service
			onError(request, new Throwable("(404) Serice not found"));
			
		} else {
			// other status code, may cannot connect server
			onError(request, new Throwable("(" + response.getStatusCode() + 
					") " + response.getStatusText()));
			
		}
		
	}
	
	public abstract void onSuccess(Request request, String data);

}
