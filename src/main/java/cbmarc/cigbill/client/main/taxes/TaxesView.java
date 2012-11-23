package cbmarc.cigbill.client.main.taxes;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Tax;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(TaxesViewImpl.class)
public interface TaxesView extends IsWidget, EditorView<Tax> {

	void setPresenter(Presenter presenter);

	void setList(List<Tax> data);

	void showFormPanel(String legendText);

	void showCellTablePanel();

	void setFormErrors(String error);

	void setFormDeleteButtonVisible(boolean visible);

	void setFieldError(String field, String error);

	public interface Presenter {
		void doAdd();

		void doLoad();

		void doSave();

		void doDelete();

		void doDelete(Set<Tax> list);

		void goTo(Place place);

	}

}
