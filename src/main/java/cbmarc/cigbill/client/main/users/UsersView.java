package cbmarc.cigbill.client.main.users;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.User;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(UsersViewImpl.class)
public interface UsersView extends IsWidget, EditorView<User> {
	
	void setPresenter(Presenter presenter);
		
	void setList(List<User> data);
	String getConfirmPassword();
	
	void showFormPanel(String legendText);
	void showCellTablePanel();
	
	void setFormErrors(String error);
	void setFieldError(String field, String error);
	
	public interface Presenter {
		public void doAdd();
		public void doLoad();
		public void doSave();
		public void doDelete(Set<User> list);
				
		void goTo(Place place);
		
	}

}
