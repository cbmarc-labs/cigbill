package cbmarc.cigbill.client.main.products;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Product;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface ProductsView extends IsWidget, EditorView<Product> {
	
	void setPresenter(Presenter presenter);
		
	void setList(List<Product> data);
	
	void showFormPanel(String legendText);
	void showCellTablePanel();
	
	void setFormErrors(String error);
	void setFieldError(String field, String error);
	
	public interface Presenter {
		public void doAdd();
		public void doLoad();
		public void doSave();
		public void doDelete(Set<Product> list);
				
		void goTo(Place place);
		
	}

}
