package cbmarc.cigbill.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;

public class AppBreadcrumb extends UlPanel {

	private String divider = "/";

	public AppBreadcrumb() {
		super();
		
		setStyleName("breadcrumb");
	}
	
	public void addCrumb(String crumb, Boolean hasDivider) {
		LiPanel liPanel = new LiPanel();
		liPanel.setStyleName("active");
		liPanel.setText(crumb);
		
		if(hasDivider)
			liPanel.add(getDivider());

		add(liPanel);
		
	}
	
	private Element getDivider() {
		SpanElement spanElement = Document.get().createSpanElement();
		spanElement.setClassName("divider");
		spanElement.setInnerText(divider);
		
		return spanElement;
	}

}
