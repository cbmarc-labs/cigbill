package cbmarc.cigbill.client.ui;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;

public interface CellTableResource extends Resources {
	//@Source({CellTable.Style.DEFAULT_CSS, "CellTable.css"})
	@Source({"AppCellTable.css"})
	TableStyle cellTableStyle();
	
	/*
	@Source("blank.jpg")
    ImageResource cellTableSortAscending();

    @Source("blank.jpg")
    ImageResource cellTableSortDescending();
	*/
	
	interface TableStyle extends CellTable.Style {}

}
