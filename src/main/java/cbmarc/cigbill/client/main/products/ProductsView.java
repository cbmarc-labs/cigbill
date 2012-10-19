package cbmarc.cigbill.client.main.products;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Product;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(ProductsViewImpl.class)
public interface ProductsView extends IsWidget, EditorView<Product> {

	void setPresenter(Presenter presenter);

	void setList(List<Product> data);

	void showFormPanel(String legendText);

	void showCellTablePanel();

	void setFormErrors(String error);

	void setFieldError(String field, String error);

	Button getFormDeleteButton();

	public interface Presenter {
		void doAdd();

		void doLoad();

		void doSave();

		void doDelete(Set<Product> list);

		void doDelete();

		void goTo(Place place);

	}

}
