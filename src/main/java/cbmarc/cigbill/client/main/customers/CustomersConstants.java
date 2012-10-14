package cbmarc.cigbill.client.main.customers;

import com.google.gwt.i18n.client.Constants;

public interface CustomersConstants extends Constants {
	
	// FORM
	@DefaultStringValue("Add customer")
	String addLegendLabel();
	
	@DefaultStringValue("Edit customer")
	String editLegendLabel();
	
	@DefaultStringValue("Name")
	String formName();

	@DefaultStringValue("Email")
	String formEmail();
}
