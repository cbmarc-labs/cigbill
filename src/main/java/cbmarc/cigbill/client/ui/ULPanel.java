package cbmarc.cigbill.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class ULPanel extends ComplexPanel {

	public ULPanel() {
		setElement(DOM.createElement("UL"));
	}

	@Override
	public void add(Widget child) {
		super.add(child, getElement());
	}

}
