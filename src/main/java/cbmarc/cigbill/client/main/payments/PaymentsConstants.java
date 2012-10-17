package cbmarc.cigbill.client.main.payments;

import com.google.gwt.i18n.client.Constants;

public interface PaymentsConstants extends Constants {
	
	// COLUMNS
	@DefaultStringValue("Name")
	String columnName();
	
	@DefaultStringValue("Description")
	String columnDescription();
	
	// FORM
	@DefaultStringValue("Add Payment")
	String addLegendLabel();
	
	@DefaultStringValue("Edit Payment")
	String editLegendLabel();
	
	@DefaultStringValue("Name")
	String formName();
	
	@DefaultStringValue("Description")
	String formDescription();

}
