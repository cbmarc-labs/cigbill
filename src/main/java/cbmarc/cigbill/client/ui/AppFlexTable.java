package cbmarc.cigbill.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;

public class AppFlexTable<T> extends FlexTable {

	private final List<ColumnFlexTable<T>> columns = new ArrayList<ColumnFlexTable<T>>();

	public void addColumn(ColumnFlexTable<T> col, Widget widget) {
		columns.add(col);

		HorizontalAlignmentConstant hAlign = col.getHorizontalAlignment();

		if (hAlign != null)
			getCellFormatter().setHorizontalAlignment(0, columns.size() - 1,
					hAlign);

		getCellFormatter().setStyleName(0, columns.size() - 1,
				"app-flex-table-header");
		setWidget(0, columns.size() - 1, widget);
	}
	
	/*public void addColumn1(ColumnFlexTable<T> col, Widget widget) {
		columns.add(col);
		
		Element tr = DOM.getFirstChild(tHeadElement);
		Element th = DOM.createTH();
		
		HorizontalAlignmentConstant hAlign = col.getHorizontalAlignment();
		
		if(hAlign != null)
			th.setAttribute("align", hAlign.getTextAlignString());
		
		DOM.appendChild(tr, th);
		
		Element lastth = DOM.getChild(DOM.getFirstChild(tHeadElement), columns.size()-1);
		
		//th.setInnerHTML(widget.getElement());
		
		//DOM.appendChild(tr, th);
		
		DOM.appendChild(lastth, widget.getElement());
	}*/

	public void setColumnWidth(ColumnFlexTable<T> column, String width) {
		ColumnFormatter cf = getColumnFormatter();

		cf.setWidth(columns.indexOf(column), width);
	}

	public void add(T t) {
		int row = getRowCount();
		int col = 0;

		for (ColumnFlexTable<T> c : columns) {
			HorizontalAlignmentConstant hAlign = c.getHorizontalAlignment();

			if (hAlign != null)
				getCellFormatter().setHorizontalAlignment(row, col, hAlign);

			setWidget(row, col++, c.getValue(t));
		}
	}

}
