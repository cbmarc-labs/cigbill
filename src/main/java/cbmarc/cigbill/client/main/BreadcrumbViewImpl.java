package cbmarc.cigbill.client.main;

import cbmarc.cigbill.client.ui.LIPanel;
import cbmarc.cigbill.client.ui.ULPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class BreadcrumbViewImpl extends Composite implements BreadcrumbView {

	private static Binder uiBinder = GWT
			.create(Binder.class);

	interface Binder extends UiBinder<Widget, BreadcrumbViewImpl> {
	}
	@UiField
	ULPanel breadcrumb;
	
	private Presenter presenter;

	public BreadcrumbViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void addCrumb(String text, final Place place) {
		LIPanel panel = new LIPanel();

		if (getElement().getChildCount() > 0) {
			SpanElement span = Document.get().createSpanElement();
			span.setClassName("divider");
			span.setInnerText("/");

			panel.getElement().appendChild(span);
		}

		if (place != null) {
			Anchor child = new Anchor(text);
			
			child.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					presenter.goTo(place);

				}
			});
			
			panel.add(child);
			
		} else {
			SpanElement child = Document.get().createSpanElement();
			child.setInnerText(text);
			
			panel.setStyleName("active");
			panel.getElement().appendChild(child);
			
		}
		
		breadcrumb.add(panel);
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public void clear() {
		breadcrumb.clear();
		
	}

}
