package cbmarc.cigbill.client.utils;

public class JavaScriptUtils {

	public static native void modal(String id, String visibility) /*-{
		$wnd.jQuery('#' + id).modal(visibility);		​
	}-*/;

}
