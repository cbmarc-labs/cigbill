package cbmarc.cigbill.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

public class AppNotify {

	private static String INFO = "info";
	private static String NOTICE = "notice";
	private static String SUCCESS = "success";
	private static String ERROR = "error";

	private static boolean showing = false;

	private static HandlerRegistration nativePreviewHandlerRegistration;

	public static void info(String text) {
		show(text, INFO);
	}

	public static void notice(String text) {
		show(text, NOTICE);
	}

	public static void success(String text) {
		String icon = "<i class=\"icon-ok icon-white\" />&nbsp;&nbsp;&nbsp;";

		show(icon + text, SUCCESS);
	}

	public static void error(String text) {
		String icon = "<i class=\"icon-remove icon-white\" />&nbsp;&nbsp;&nbsp;";

		show(icon + text, ERROR);
	}

	private static void show(String text, String type) {
		showing = true;
		updateHandlers();
		pnotifyShow(text, type);
	}

	private static void hide() {
		showing = false;
		updateHandlers();
		pnotifyHide();
	}

	private static void previewNativeEvent(NativePreviewEvent event) {
		Event nativeEvent = Event.as(event.getNativeEvent());
		int type = nativeEvent.getTypeInt();

		switch (type) {
		case Event.ONCLICK:
			hide();
		}
	}

	private static void updateHandlers() {
		if (nativePreviewHandlerRegistration != null) {
			nativePreviewHandlerRegistration.removeHandler();
			nativePreviewHandlerRegistration = null;
		}

		if (showing) {
			nativePreviewHandlerRegistration = Event
					.addNativePreviewHandler(new NativePreviewHandler() {
						public void onPreviewNativeEvent(
								NativePreviewEvent event) {
							previewNativeEvent(event);
						}
					});
		}
	}

	private static native void pnotifyHide() /*-{
		$wnd.$.pnotify_remove_all();
	}-*/;

	private static native void pnotifyShow(String text, String type) /*-{
		$wnd.$.pnotify({
			text: text,
			type: type, // notice, info, success, error
			sticker: false,
			closer: false,
			addclass: "custom",
			animate_speed: "fast",
			hide: false,
			icon: false,
			history: false});​
	}-*/;
}
