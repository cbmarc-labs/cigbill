package cbmarc.cigbill.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;

public class AppFlexTable<T> extends FlexTable {

	private final List<ColumnFlexTable<T>> columns = new ArrayList<ColumnFlexTable<T>>();

	private List<T> list = new ArrayList<T>();

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

	public void setColumnWidth(ColumnFlexTable<T> column, String width) {
		ColumnFormatter cf = getColumnFormatter();

		cf.setWidth(columns.indexOf(column), width);
	}

	public void setList(List<T> items) {
		clear();

		if (items != null) {
			for (T t : items) {
				add(t);
			}
		}

	}

	public List<T> getList() {
		return list;
	}

	@Override
	public void clear() {
		list.clear();

		for (int i = getRowCount() - 1; i > 0; i--) {
			super.removeRow(i);
		}
	}

	@Override
	public void removeRow(int row) {
		list.remove(row - 1);

		super.removeRow(row);
	}

	public void add(T t) {
		int row = getRowCount();
		int col = 0;

		list.add(t);

		for (ColumnFlexTable<T> c : columns) {
			HorizontalAlignmentConstant hAlign = c.getHorizontalAlignment();

			if (hAlign != null)
				getCellFormatter().setHorizontalAlignment(row, col, hAlign);
			
			setWidget(row, col++, c.getValue(t));
		}
	}

}
