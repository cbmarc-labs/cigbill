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
	
	@DefaultStringValue("Date")
	String formDate();
	
	@DefaultStringValue("Tax")
	String formTax();
	
	@DefaultStringValue("Discount")
	String formDiscount();
	
	@DefaultStringValue("Shipping")
	String formShipping();
	
	@DefaultStringValue("Notes")
	String formNotes();
	
	// TABS
	@DefaultStringValue("General")
	String tabGeneral();
	
	@DefaultStringValue("Items")
	String tabItems();
	
	@DefaultStringValue("Notes")
	String tabNotes();
	
	// MODAL
	@DefaultStringValue("Select Customer")
	String modalSelectCustomer();
	
	@DefaultStringValue("Select Item")
	String modalSelectItem();
	
	// OTHERS
	@DefaultStringValue("New Line")
	String newLine();
	
	// ITEMS SECTION
	@DefaultStringValue("Subtotal")
	String invoiceSubtotal();
	
	@DefaultStringValue("Tax")
	String invoiceTax();
	
	@DefaultStringValue("Discount")
	String invoiceDiscount();
	
	@DefaultStringValue("Shipping")
	String invoiceShipping();
	
	@DefaultStringValue("Total")
	String invoiceTotal();
}
