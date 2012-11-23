package cbmarc.cigbill.client.main.invoices;

import com.google.gwt.i18n.client.Constants;

public interface InvoicesConstants extends Constants {
	
	// COLUMNS
	@DefaultStringValue("Id")
	String columnId();
	
	// FORM
	@DefaultStringValue("Add Invoice")
	String addLegendLabel();
	
	@DefaultStringValue("Edit Invoice")
	String editLegendLabel();
	
	@DefaultStringValue("Notes")
	String formNotes();
	
	// TABS
	@DefaultStringValue("General")
	String tabGeneral();
	
	@DefaultStringValue("Notes")
	String tabNotes();

}
