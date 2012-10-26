package cbmarc.cigbill.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.ui.Anchor;

public class AppBreadcrumb extends UlPanel {

	private HandlerManager handlerManager = new HandlerManager(this);

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
	
	public void addCrumb(String crumb, final Place place) {
		LiPanel liPanel = new LiPanel();
		
		Anchor anchor = new Anchor(crumb, "javascript:;");
		liPanel.add(anchor);
		
		anchor.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				handlerManager.fireEvent(new PlaceChangeEvent(place));
				
			}});
		
		liPanel.add(getDivider());

		add(liPanel);
		
	}
	
	private Element getDivider() {
		SpanElement spanElement = Document.get().createSpanElement();
		spanElement.setClassName("divider");
		spanElement.setInnerText(divider);
		
		return spanElement;
	}

	public HandlerRegistration addPlaceChangeHandler(
			PlaceChangeEvent.Handler handler) {
		return handlerManager.addHandler(PlaceChangeEvent.TYPE, handler);
	}

}
