package cbmarc.cigbill.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class LIPanel extends ComplexPanel {
	
	public LIPanel() {
		setElement(DOM.createElement("LI")); 
	}

	@Override
	public void add(Widget child) {
		super.add(child, getElement());
	}

}
