package cbmarc.cigbill.client.main.items;

import com.google.gwt.i18n.client.Constants;

public interface ItemsConstants extends Constants {
	
	// COLUMNS	
	@DefaultStringValue("Description")
	String columnDescription();

	@DefaultStringValue("Quantity")
	String columnQuantity();
	
	@DefaultStringValue("Price")
	String columnPrice();
	
	@DefaultStringValue("Total")
	String columnTotal();
	
	// FORM
	@DefaultStringValue("Add item")
	String addLegendLabel();
	
	@DefaultStringValue("Edit item")
	String editLegendLabel();
		
	@DefaultStringValue("Description")
	String formDescription();
	
	@DefaultStringValue("Quantity")
	String formQuantity();
	
	@DefaultStringValue("Price")
	String formPrice();
		
	@DefaultStringValue("Notes")
	String formNotes();
	
	// TABS
	@DefaultStringValue("General")
	String tabGeneral();
	
	@DefaultStringValue("Notes")
	String tabNotes();

}
