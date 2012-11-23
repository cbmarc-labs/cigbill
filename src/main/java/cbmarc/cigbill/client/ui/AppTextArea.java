package cbmarc.cigbill.client.ui;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.TextArea;

public class AppTextArea extends TextArea {

	@Override
	protected void onLoad() {
		super.onLoad();

		initialize(getElement());
	}

	public static native void initialize(Element e) /*-{
		$wnd.jQuery(e).TextAreaExpander();
	}-*/;

}
