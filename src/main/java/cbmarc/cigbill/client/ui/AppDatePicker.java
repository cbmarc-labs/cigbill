package cbmarc.cigbill.client.ui;

import java.util.Date;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.Widget;

public class AppDatePicker extends Widget implements LeafValueEditor<Date>,
		ChangeHandler, HasChangeHandlers {

	private TextBox textBox;
	private DateTimeFormat dtf;
	private String format;

	public AppDatePicker() {
		textBox = new TextBox();

		setFormat(DateTimeFormat.getFormat("dd/mm/yyyy").getPattern()
				.toLowerCase());

		textBox.setAlignment(TextAlignment.RIGHT);
		textBox.setStyleName("datepicker input-small");

		addChangeHandler(this);

		setElement(textBox.getElement());
	}

	public void setFormat(String format) {
		this.format = format;
		this.dtf = DateTimeFormat.getFormat(format.replaceAll("mm", "MM"));
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		initialize(getElement(), format);
	}

	public static native void initialize(Element e, String f) /*-{
		$wnd.jQuery(e).datepicker({
			format : f,
			weekStart : 1
		//, 
		//language: 'es'
		});
	}-*/;

	@Override
	public void setValue(Date value) {
		if (value == null)
			value = new Date();

		textBox.setValue(dtf.format(value));

	}

	@Override
	public Date getValue() {
		try {
			return dtf.parse(textBox.getValue());

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		try {
			setValue(dtf.parse(textBox.getValue()));

		} catch (Exception e) {
			setValue(null);
		}

	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}

}
