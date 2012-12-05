package cbmarc.cigbill.client.ui;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.DoubleBox;

public class AppDoubleBox extends DoubleBox implements LeafValueEditor<Double>,
		ChangeHandler, KeyPressHandler {

	public AppDoubleBox() {
		super();

		setStyleName("input-small");
		setAlignment(TextAlignment.RIGHT);
		setMaxLength(7);

		addChangeHandler(this);
		addKeyPressHandler(this);
	}

	@Override
	public void setValue(Double value) {
		if (value == null)
			value = 0d;

		setText(NumberFormat.getFormat("#,##0.00").format(value));

	}
	
	public String getValueAsText() {
		String value = "0.00";
		
		try {
			value = NumberFormat.getFormat("#0.00").format(super.getValue());
		} catch (Exception e) {
		}

		return value;
	}

	@Override
	public void onChange(ChangeEvent event) {
		setValue(super.getValue());

	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (!"0123456789.,".contains(String.valueOf(event.getCharCode()))) {
			event.preventDefault();
		}

	}

}
