package cbmarc.cigbill.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class TextAreaCell extends AbstractCell<String> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			String value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<textarea type=\"text\" class=\"autogrow\">"
				+ value + "</textarea>");
		
	}

}
