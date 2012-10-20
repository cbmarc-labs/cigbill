package cbmarc.cigbill.client.main.invoices;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.shared.Invoice;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InvoicesServiceAsync {

	void getAll(final AsyncCallback<List<Invoice>> callback);

	void getById(Long id, final AsyncCallback<Invoice> callback);

	void save(Invoice product, final AsyncCallback<Void> callback);

	void delete(Set<Invoice> list, final AsyncCallback<Void> callback);

	void delete(Invoice invoice, final AsyncCallback<Void> callback);

}
