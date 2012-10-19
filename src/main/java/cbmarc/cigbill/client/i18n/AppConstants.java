package cbmarc.cigbill.client.i18n;

import com.google.gwt.i18n.client.Constants;

public interface AppConstants extends Constants {

	/**
	 * Global
	 */
	@DefaultStringValue("Add new item")
	String addNewItemButton();
	
	@DefaultStringValue("Delete selected item(s)")
	String deleteSelectedItemsButton();
	
	@DefaultStringValue("Submit")
	String formSubmit();

	@DefaultStringValue("Back")
	String formBack();

	@DefaultStringValue("Delete")
	String formDelete();

	@DefaultStringValue("Refresh")
	String toolbarRefreshButton();
	
	@DefaultStringValue("* Required fields")
	String formRequiredFieldsText();
	
	@DefaultStringValue("Discard Changes ?")
	String formDiscardChanges();
	
	@DefaultStringValue("Are you sure ?")
	String areYouSure();
	
	@DefaultStringValue("Item(s) deleted.")
	String itemsDeleted();
	
	@DefaultStringValue("Item saved.")
	String itemSaved();
	
	@DefaultStringValue("Item not found.")
	String itemNotFound();

	/**
	 * NAVIGATION
	 */
	@DefaultStringValue("Money")
	String navMoney();

	@DefaultStringValue("Invoices")
	String navInvoices();

	@DefaultStringValue("Payments")
	String navPayments();

	@DefaultStringValue("Stock")
	String navStock();

	@DefaultStringValue("Products")
	String navProducts();

	@DefaultStringValue("Taxes")
	String navTaxes();

	@DefaultStringValue("People")
	String navPeople();

	@DefaultStringValue("Users")
	String navUsers();

	@DefaultStringValue("Customers")
	String navCustomers();

	@DefaultStringValue("Settings")
	String navSettings();

	@DefaultStringValue("Messages")
	String navMessages();

	@DefaultStringValue("Logout")
	String navLogout();
	
	/**
	 * BREADCRUMB
	 */
	@DefaultStringValue("Home")
	String breadcrumbHome();
	
	@DefaultStringValue("Add")
	String breadcrumbAdd();
	
	@DefaultStringValue("Edit")
	String breadcrumbEdit();
	
	@DefaultStringValue("Undefined")
	String breadcrumbUndefined();
}
