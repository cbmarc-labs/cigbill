package cbmarc.cigbill.client.main.products;

import com.google.gwt.i18n.client.Constants;

public interface ProductsConstants extends Constants {
	
	// COLUMNS
	@DefaultStringValue("Name")
	String columnName();
	
	@DefaultStringValue("Description")
	String columnDescription();
	
	@DefaultStringValue("Price")
	String columnPrice();
	
	// FORM
	@DefaultStringValue("Add product")
	String addLegendLabel();
	
	@DefaultStringValue("Edit product")
	String editLegendLabel();
	
	@DefaultStringValue("Name")
	String formName();
	
	@DefaultStringValue("Description")
	String formDescription();
	
	@DefaultStringValue("Price")
	String formPrice();
	
	@DefaultStringValue("Notes")
	String formNotes();

}
