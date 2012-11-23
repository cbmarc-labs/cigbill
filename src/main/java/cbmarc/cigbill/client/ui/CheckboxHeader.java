package cbmarc.cigbill.client.ui;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasValue;

public class CheckboxHeader extends Header<Boolean> implements
		HasValue<Boolean> {

	private boolean checked;

	private HandlerManager handlerManager = new HandlerManager(this);

	public CheckboxHeader() {
		super(new CheckboxCell());
		checked = false;
	}

	@Override
	public Boolean getValue() {
		return checked;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Boolean> handler) {
		return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);

	}

	@Override
	public void setValue(Boolean value) {
		checked = value;

	}

	@Override
	public void onBrowserEvent(Context context, Element elem, NativeEvent event) {
		int eventType = Event.as(event).getTypeInt();
		if (eventType == Event.ONCHANGE) {
			event.preventDefault();
			// use value setter to easily fire change event to handlers
			setValue(!checked, true);
		}
	}

	@Override
	public void setValue(Boolean value, boolean fireEvents) {
		checked = value;

		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}

	}

}
