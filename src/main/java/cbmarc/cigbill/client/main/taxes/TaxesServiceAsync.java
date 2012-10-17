package cbmarc.cigbill.client.main.taxes;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Tax;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TaxesServiceAsync {
	
	void getAll(final AsyncCallback<List<Tax>> callback);
	void getById(String id, final AsyncCallback<Tax> callback);
	void save(Tax product, final AsyncCallback<Void> callback);
	void delete(Set<Tax> list, final AsyncCallback<Void> callback);
	
}
