package cbmarc.cigbill.client.ui;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.TextBox;

public class Datepicker extends TextBox {

	private DateTimeFormat dtf;
	private String format;

	public Datepicker() {
		setFormat(DateTimeFormat
				.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT)
				.getPattern().toLowerCase());
		setValue(new Date());
	}

	public void setFormat(String format) {
		this.format = format;
		this.dtf = DateTimeFormat.getFormat(format.replaceAll("mm", "MM"));

		if (getValue() != null) {
			setValue(new Date());
		}

	}

	public void setValue(Date value) {
		if (value != null)
			setValue(dtf.format(value));
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		initialize(getElement(), format);
	}

	public static native void initialize(Element e, String f) /*-{
		$wnd.jQuery(e).datepicker({
			format : f,
			autoclose : true,
			weekStart : 1
		//, 
		//language: 'es'
		});
	}-*/;

}
