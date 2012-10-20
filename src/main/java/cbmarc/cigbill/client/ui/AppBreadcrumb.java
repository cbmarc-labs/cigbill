package cbmarc.cigbill.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.Anchor;

public class AppBreadcrumb extends UlPanel {

	private String divider = "/";

	public AppBreadcrumb() {
		super();
		
		setStyleName("breadcrumb");
	}
	
	public void addCrumb(String crumb) {
		LiPanel liPanel = new LiPanel();
		liPanel.setStyleName("active");
		liPanel.setText(crumb);
		
		liPanel.add(getDivider());

		add(liPanel);
	}
	
	public void addCrumb(String crumb, String url) {
		LiPanel liPanel = new LiPanel();
		liPanel.add(new Anchor(crumb, url));
		
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
