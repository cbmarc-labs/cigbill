package cbmarc.cigbill.client.ui;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;

public class AppTab extends Anchor {
	
	@Override
	protected void onLoad() {
		super.onLoad();
		initialize(getElement());
	}

	public static native void initialize(Element e) /*-{
		$wnd.jQuery(e).click(function(e) {
			e.preventDefault();
			$wnd.jQuery(this).tab('show');
		});​
	}-*/;

}
