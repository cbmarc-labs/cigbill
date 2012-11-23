package cbmarc.cigbill.client.ui;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

public abstract class ColumnFlexTable<T> implements HasHorizontalAlignment {

	private HorizontalAlignmentConstant hAlign = null;

	public abstract Widget getValue(T object);

	@Override
	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return hAlign;
	}

	@Override
	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		this.hAlign = align;
		
	}

}
