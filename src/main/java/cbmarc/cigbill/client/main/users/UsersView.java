package cbmarc.cigbill.client.main.users;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.User;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(UsersViewImpl.class)
public interface UsersView extends IsWidget, EditorView<User> {

	void setList(List<User> data);

	void showFormPanel(String legendText);

	void showCellTablePanel();

	void setFormErrors(String error);

	void setFieldError(String field, String error);
	
	Button getFormDeleteButton();

	void setPresenter(Presenter presenter);

	public interface Presenter {
		void doAdd();

		void doLoad();

		void doSave();

		void doDelete();

		void doDelete(Set<User> list);

		void goTo(Place place);

	}

}
