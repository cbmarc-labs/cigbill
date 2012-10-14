package cbmarc.cigbill.client.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class AppMessage extends PopupPanel {

	public static String[] INFO = new String[] { "alert-info", "icon-info-sign" };
	public static String[] SUCCESS = new String[] { "alert-success", "icon-ok" };
	public static String[] WARNING = new String[] { "", "icon-warning-sign" };
	public static String[] ERROR = new String[] { "alert-error", "icon-remove" };

	/**
	 * @param message
	 * @param style
	 */
	public AppMessage(String message, String style[]) {
		super(false);

		AppMessageAnimation showAnimation = new AppMessageAnimation();

		if (message.length() > 60)
			message = message.substring(0, 60) + " ...";

		HTMLPanel messagePanel = new HTMLPanel("<i class=\"" + style[1]
				+ " icon-white\"></i>" + message);

		setAutoHideOnHistoryEventsEnabled(true);
		setAutoHideEnabled(true);
		setStyleName("alert " + style[0]);
		setWidget(messagePanel);

		showAnimation.run(500);
		show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.PopupPanel#show()
	 */
	@Override
	public void show() {
		super.show();

		int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
		setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), -3);
	}

	/**
	 * Hide popup when key pressed
	 */
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);

		switch (event.getTypeInt()) {
		case Event.ONKEYDOWN:
			hide();
		}
	}

	class AppMessageAnimation extends Animation {

		@Override
		protected void onUpdate(double progress) {
			double opacityValue = progress;

			AppMessage.this.getElement().getStyle().setOpacity(opacityValue);

		}

	}

}
