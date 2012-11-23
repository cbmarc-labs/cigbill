package cbmarc.cigbill.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.ScriptElement;

public class ResourcesInjector {

	private static HeadElement head;

	public static void configure() {
		injectCssAsFile("bootstrap.modified.css");
		injectCssAsFile("bootstrap-responsive.min.css");
		injectCssAsFile("datepicker.css");
		injectCssAsFile("jquery.pnotify.default.css");
		injectCssAsFile("application.css");

		injectJs(Resources.INSTANCE.jquery().getText());
		injectJs(Resources.INSTANCE.textareaExpander().getText());
		injectJs(Resources.INSTANCE.pnotify().getText());
		injectJs(Resources.INSTANCE.bootstrap().getText());
		injectJs(Resources.INSTANCE.datepicker().getText());
	}

	private static HeadElement getHead() {
		if (head == null) {
			Element elt = Document.get().getElementsByTagName("head")
					.getItem(0);

			head = HeadElement.as(elt);
		}

		return head;
	}

	public static void injectCssAsFile(String filename) {
		LinkElement link = Document.get().createLinkElement();

		link.setType("text/css");
		link.setRel("stylesheet");
		link.setHref(GWT.getModuleName() + "/css/" + filename);

		getHead().appendChild(link);
	}

	public static void injectJs(String text) {
		HeadElement head = getHead();
		ScriptElement element = createScriptElement();
		element.setText(text);
		
		head.appendChild(element);
	}

	private static ScriptElement createScriptElement() {
		ScriptElement script = Document.get().createScriptElement();
		
		script.setAttribute("type", "text/javascript");
		script.setAttribute("charset", "UTF-8");
		
		return script;
	}

}
