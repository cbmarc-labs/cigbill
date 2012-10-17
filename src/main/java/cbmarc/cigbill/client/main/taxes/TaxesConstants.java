package cbmarc.cigbill.client.main.taxes;

import com.google.gwt.i18n.client.Constants;

public interface TaxesConstants extends Constants {
	
	// COLUMNS
	@DefaultStringValue("Name")
	String columnName();
	
	@DefaultStringValue("Description")
	String columnDescription();
	
	// FORM
	@DefaultStringValue("Add tax")
	String addLegendLabel();
	
	@DefaultStringValue("Edit tax")
	String editLegendLabel();
	
	@DefaultStringValue("Name")
	String formName();
	
	@DefaultStringValue("Description")
	String formDescription();

}
