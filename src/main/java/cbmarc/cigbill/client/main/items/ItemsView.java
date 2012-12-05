package cbmarc.cigbill.client.main.items;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Item;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(ItemsViewImpl.class)
public interface ItemsView extends IsWidget, EditorView<Item> {

	void setPresenter(Presenter presenter);

	void setList(List<Item> data);

	void showFormPanel(String legendText);

	void showCellTablePanel();

	void setFormErrors(String error);

	void setFieldError(String field, String error);

	void setFormDeleteButtonVisible(boolean visible);

	public interface Presenter {
		void doAdd();

		void doLoad();

		void doSave();

		void doDelete(Set<Item> list);

		void doDelete();

		void goTo(Place place);

	}

}
