package cbmarc.cigbill.client.ui;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.ListBox;

public class ListBoxEditor extends ListBox implements LeafValueEditor<String> {

	@Override
	public void setValue(String value) {
		// deselect
		for (int i = 0; i < getItemCount(); i++)
			setItemSelected(i, false);
				
		if(value == null)
			return;
		
		for(int i=0; i<getItemCount(); i++) {			
			if(getValue(i).equals(value)) {
				setItemSelected(i, true);
				break;
			}
		}
		
	}

	@Override
	public String getValue() {
		String value = getValue(getSelectedIndex());
		
		return value;
	}

}
