package cbmarc.cigbill.client.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;

public class JSONRequest {

	public JSONRequest(String url, String json, 
			JSONRequestCallback callback) throws Exception {
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
		builder.setHeader("Content-type", "application/json; charset=utf-8");
		builder.setTimeoutMillis(20000);
		
		Request request = builder.sendRequest(json, callback);
	}
	
}
