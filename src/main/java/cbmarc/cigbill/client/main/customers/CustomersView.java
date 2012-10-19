package cbmarc.cigbill.client.main.customers;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Customer;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(CustomersViewImpl.class)
public interface CustomersView extends IsWidget, EditorView<Customer> {

	void setPresenter(Presenter presenter);

	void setList(List<Customer> data);

	void showFormPanel(String legendText);

	void showCellTablePanel();

	void setFormErrors(String error);

	void setFieldError(String field, String error);

	Button getFormDeleteButton();

	public interface Presenter {
		void doAdd();

		void doLoad();

		void doSave();

		void doDelete(Set<Customer> list);

		void doDelete();

		void goTo(Place place);

	}

}
