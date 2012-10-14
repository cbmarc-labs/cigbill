package cbmarc.cigbill.client.main.customers;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Customer;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface CustomersView extends IsWidget, EditorView<Customer> {
		
	void setList(List<Customer> data);
	
	void showFormPanel(String legendText);
	void showCellTablePanel();
	
	void setFormErrors(String error);
	void setFieldError(String field, String error);
	
	public interface Presenter {
		public void doAdd();
		public void doLoad();
		public void doSave();
		public void doDelete(Set<Customer> list);
				
		void goTo(Place place);
		
	}

}
