package cbmarc.cigbill.client.ui;

import cbmarc.cigbill.client.utils.JavaScriptUtils;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.Window;

/**
 * An {@link AbstractCell} used to render a text input.
 */
public class TextAreaInputCell extends
		AbstractInputCell<String, TextAreaInputCell.ViewData> {

	interface Template extends SafeHtmlTemplates {
		@Template("<textarea type=\"text\" class=\"autogrow\">{0}</textarea>")
		SafeHtml input(String value);
	}

	/**
	 * The {@code ViewData} for this cell.
	 */
	public static class ViewData {
		/**
		 * The last value that was updated.
		 */
		private String lastValue;

		/**
		 * The current value.
		 */
		private String curValue;

		/**
		 * Construct a ViewData instance containing a given value.
		 * 
		 * @param value
		 *            a String value
		 */
		public ViewData(String value) {
			this.lastValue = value;
			this.curValue = value;
		}

		/**
		 * Return true if the last and current values of this ViewData object
		 * are equal to those of the other object.
		 */
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof ViewData)) {
				return false;
			}
			ViewData vd = (ViewData) other;
			return equalsOrNull(lastValue, vd.lastValue)
					&& equalsOrNull(curValue, vd.curValue);
		}

		/**
		 * Return the current value of the input element.
		 * 
		 * @return the current value String
		 * @see #setCurrentValue(String)
		 */
		public String getCurrentValue() {
			return curValue;
		}

		/**
		 * Return the last value sent to the {@link ValueUpdater}.
		 * 
		 * @return the last value String
		 * @see #setLastValue(String)
		 */
		public String getLastValue() {
			return lastValue;
		}

		/**
		 * Return a hash code based on the last and current values.
		 */
		@Override
		public int hashCode() {
			return (lastValue + "_*!@HASH_SEPARATOR@!*_" + curValue).hashCode();
		}

		/**
		 * Set the current value.
		 * 
		 * @param curValue
		 *            the current value
		 * @see #getCurrentValue()
		 */
		protected void setCurrentValue(String curValue) {
			this.curValue = curValue;
		}

		/**
		 * Set the last value.
		 * 
		 * @param lastValue
		 *            the last value
		 * @see #getLastValue()
		 */
		protected void setLastValue(String lastValue) {
			this.lastValue = lastValue;
		}

		private boolean equalsOrNull(Object a, Object b) {
			return (a != null) ? a.equals(b) : ((b == null) ? true : false);
		}
	}

	private static Template template;

	/**
	 * Constructs a TextInputCell that renders its text without HTML markup.
	 */
	public TextAreaInputCell() {
		super(BrowserEvents.CHANGE, BrowserEvents.KEYUP);
		if (template == null) {
			template = GWT.create(Template.class);
		}
	}

	/**
	 * Constructs a TextInputCell that renders its text using the given
	 * {@link SafeHtmlRenderer}.
	 * 
	 * @param renderer
	 *            parameter is ignored
	 * @deprecated the value of a text input is never treated as html
	 */
	@Deprecated
	public TextAreaInputCell(SafeHtmlRenderer<String> renderer) {
		this();
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent,
			String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, String value,
			NativeEvent event, ValueUpdater<String> valueUpdater) {
		//super.onBrowserEvent(context, parent, value, event, valueUpdater);

		// Ignore events that don't target the input.
		InputElement input = getInputElement(parent);
		Element target = event.getEventTarget().cast();
		if (!input.isOrHasChild(target)) {
			return;
		}

		String eventType = event.getType();
		Object key = context.getKey();
		if (BrowserEvents.CHANGE.equals(eventType)) {
			// finishEditing(parent, value, key, valueUpdater);
		} else if (BrowserEvents.KEYUP.equals(eventType)) {
			// Record keys as they are typed.
			ViewData vd = getViewData(key);
			if (vd == null) {
				vd = new ViewData(value);
				setViewData(key, vd);
			}
			vd.setCurrentValue(input.getValue());
			// adjustHeight4(parent);

		}
	}

	private void adjustHeight4(Element parent) {
		InputElement input = getInputElement(parent);

		input.setAttribute("style", "overflow:hidden;");

		int newHeight = input.getScrollHeight();
		int currentHeight = input.getClientHeight();

		if (newHeight > currentHeight) {
			String height = newHeight + 6 + "px;";

			input.setAttribute("style", "overflow:hidden;height:" + height);
		}
	}

	private void adjustHeight3(Element parent) {
		InputElement input = getInputElement(parent);

		String height = input.getScrollHeight() + "px";

		input.setAttribute("style", "");
		input.setAttribute("style", "height:" + height);
	}

	private void adjustHeight2(Element parent) {
		InputElement input = getInputElement(parent);

		int rows = input.getValue().split("\r\n|\r|\n").length;

		input.setAttribute("rows", String.valueOf(rows));
	}

	private void adjustHeight1(Element parent) {
		InputElement input = getInputElement(parent);

		String[] lines = input.getValue().split("\n");

		int newRows = lines.length + 1;
		int cols = Integer.parseInt(input.getAttribute("cols"));
		int rows = Integer.parseInt(input.getAttribute("rows"));

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];

			if (line.length() >= cols)
				newRows += Math.floor(line.length() / cols);
		}

		if (newRows > rows)
			rows = newRows;
		if (newRows < rows)
			rows = Math.max(2, newRows);

		input.setAttribute("rows", String.valueOf(rows));
	}

	private void adjustHeight(Element parent) {
		InputElement input = getInputElement(parent);

		int clientHeight = input.getClientHeight();
		int scrollHeight = input.getScrollHeight();
		String height = "0px";

		if (clientHeight < scrollHeight) {
			height = scrollHeight + "px";

			if (clientHeight < scrollHeight) {
				height = (scrollHeight * 2 - clientHeight) + "px";
			}

			input.setAttribute("style", "height:" + height);
		}
	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		// Get the view data.
		Object key = context.getKey();
		ViewData viewData = getViewData(key);
		if (viewData != null && viewData.getCurrentValue().equals(value)) {
			clearViewData(key);
			viewData = null;
		}

		String s = (viewData != null) ? viewData.getCurrentValue() : value;
		if (s != null) {
			sb.append(template.input(s));
		} else {
			sb.appendHtmlConstant("<textarea type=\"text\" class=\"autogrow\"></textarea>");


		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				//JavaScriptUtils.autogrow();

			}
		});
		}
	}

	@Override
	public void setValue(Context context, Element parent, String value) {
		// TODO Auto-generated method stub
		//super.setValue(context, parent, value);
	}

	@Override
	protected void finishEditing(Element parent, String value, Object key,
			ValueUpdater<String> valueUpdater) {
		String newValue = getInputElement(parent).getValue();

		// Get the view data.
		ViewData vd = getViewData(key);
		if (vd == null) {
			vd = new ViewData(value);
			setViewData(key, vd);
		}
		vd.setCurrentValue(newValue);

		// Fire the value updater if the value has changed.
		if (valueUpdater != null
				&& !vd.getCurrentValue().equals(vd.getLastValue())) {
			vd.setLastValue(newValue);
			valueUpdater.update(newValue);
		}

		// Blur the element.
		super.finishEditing(parent, newValue, key, valueUpdater);
	}

	@Override
	protected InputElement getInputElement(Element parent) {
		return super.getInputElement(parent).<InputElement> cast();
	}

}
