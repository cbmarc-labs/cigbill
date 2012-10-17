package cbmarc.cigbill.client.main.taxes;

import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.utils.EditorView;
import cbmarc.cigbill.shared.Tax;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface TaxesView extends IsWidget, EditorView<Tax> {
	
	void setPresenter(Presenter presenter);
		
	void setList(List<Tax> data);
	
	void showFormPanel(String legendText);
	void showCellTablePanel();
	
	void setFormErrors(String error);
	void setFieldError(String field, String error);
	
	public interface Presenter {
		public void doAdd();
		public void doLoad();
		public void doSave();
		public void doDelete(Set<Tax> list);
				
		void goTo(Place place);
		
	}

}
