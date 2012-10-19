package cbmarc.cigbill.client.main.invoices;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Invoice;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(InvoicesViewImpl.class)
public interface InvoicesView extends IsWidget, EditorView<Invoice> {

	void setPresenter(Presenter presenter);

	void setList(List<Invoice> data);

	void showFormPanel(String legendText);

	void showCellTablePanel();

	void setFormErrors(String error);

	void setFieldError(String field, String error);

	Button getFormDeleteButton();

	public interface Presenter {
		void doAdd();

		void doLoad();

		void doSave();

		void doDelete(Set<Invoice> list);

		void doDelete();

		void goTo(Place place);

	}

}
