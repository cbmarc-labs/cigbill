package cbmarc.cigbill.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.ListBox;

public class ListBoxMultiEditor extends ListBox 
		implements LeafValueEditor<List<String>> {

	public ListBoxMultiEditor() {
		super(true);

	}

	@Override
	public void setValue(List<String> value) {
		// deselect all
		for (int i = 0; i < getItemCount(); i++)
			setItemSelected(i, false);
		
		if (value == null) {
            return;
        }
 
        for (String v : value) {
            for (int i = 0; i < getItemCount(); i++) {            	
                if (getValue(i).equals(v)) {
                    setItemSelected(i, true);
                }
            }
        }
		
	}

	@Override
	public List<String> getValue() {
		List<String> values = new ArrayList<String>();
        for (int i = 0; i < getItemCount(); i++) {
            if (isItemSelected(i)) {
                values.add(getValue(i));
            }
        }
 
        return values;
	}

}
