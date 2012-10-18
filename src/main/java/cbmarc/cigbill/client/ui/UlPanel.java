package cbmarc.cigbill.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class UlPanel extends ComplexPanel {

	public UlPanel() {
		setElement(DOM.createElement("UL"));
	}
	
	public void add(Widget w) { 
		add(w, getElement());
	}
	
	public void add(Element e) {
		getElement().appendChild(e);
	}

}
