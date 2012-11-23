package cbmarc.cigbill.client.main.products;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

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
	
	@DefaultStringValue("Tax")
	String formTax();
	
	@DefaultStringValue("Notes")
	String formNotes();
	
	// TABS
	@DefaultStringValue("General")
	String tabGeneral();
	
	@DefaultStringValue("Notes")
	String tabNotes();

}
