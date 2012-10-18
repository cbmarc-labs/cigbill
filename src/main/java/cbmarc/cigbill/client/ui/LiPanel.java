package cbmarc.cigbill.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class LiPanel extends ComplexPanel implements HasText {

	public LiPanel() {
		setElement(DOM.createElement("LI")); 
	}
	
	public void add(Widget w) { 
		add(w, getElement());
	}
	
	public void add(Element e) {
		getElement().appendChild(e);
	}

	@Override
	public String getText() {
		return DOM.getInnerText(getElement()); 
	}

	@Override
	public void setText(String text) {
		DOM.setInnerText(getElement(), (text == null) ? "" : text); 
		
	} 

}
