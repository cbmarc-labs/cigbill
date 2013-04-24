package cbmarc.cigbill.client.main.invoices;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.customers.CustomersConstants;
import cbmarc.cigbill.client.main.products.ProductsConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.ui.AppCheckboxCellTable;
import cbmarc.cigbill.client.ui.AppDatePicker;
import cbmarc.cigbill.client.ui.AppDoubleBox;
import cbmarc.cigbill.client.ui.AppFlexTable;
import cbmarc.cigbill.client.ui.AppTextArea;
import cbmarc.cigbill.client.ui.ColumnFlexTable;
import cbmarc.cigbill.client.utils.JavaScriptUtils;
import cbmarc.cigbill.shared.Customer;
import cbmarc.cigbill.shared.Invoice;
import cbmarc.cigbill.shared.Product;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InvoicesViewImpl extends Composite implements InvoicesView,
		Editor<Invoice> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, InvoicesViewImpl> {
	}

	public interface Driver extends
			SimpleBeanEditorDriver<Invoice, InvoicesViewImpl> {
	}

	@UiField
	AppCheckboxCellTable<Invoice> invoicesTable;

	@UiField
	FormPanel formPanel;

	@Editor.Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel tablePanel;

	MultiWordSuggestOracle customerSuggestions = new MultiWordSuggestOracle();

	@Editor.Ignore
	@UiField(provided = true)
	SuggestBox customerName = new SuggestBox(customerSuggestions);

	Customer customerField;
	LeafValueEditor<Customer> customer = new LeafValueEditor<Customer>() {

		@Override
		public void setValue(Customer value) {
			customerName.setValue("");

			if (value != null)
				customerName.setValue(value.getName());

		}

		@Override
		public Customer getValue() {
			return customerField;
		}
	};

	@UiField
	AppDatePicker date;

	@UiField
	AppDoubleBox tax, discount, shipping;

	LeafValueEditor<List<Product>> items = new LeafValueEditor<List<Product>>() {

		@Override
		public void setValue(List<Product> value) {
			itemsInvoicesTable.setList(value);

		}

		@Override
		public List<Product> getValue() {
			List<Product> items = itemsInvoicesTable.getList();

			for (int i = 1; i < itemsInvoicesTable.getRowCount(); i++) {
				Product product = items.get(i - 1);
			}

			return itemsInvoicesTable.getList();
		}
	};

	@UiField
	TextArea notes;

	@UiField
	HTMLPanel validationPanel;

	@UiField
	DivElement notesCG;

	@UiField
	Button addTableButton, deleteTableButton, toolbarRefreshButton,
			selectCustomerModalCancel, validationButton, selectCustomerButton,
			addItemButton, deleteProductButton, selectItemsModalOk,
			selectItemsModalCancel, backButton, formDeleteButton,
			addNewLine;

	@UiField
	SubmitButton submitButton;

	@UiField
	AppCellTable<Customer> customersTable;

	@UiField
	AppFlexTable<Product> itemsInvoicesTable;

	private CheckBox checkboxProductsInvoicesHeader;

	@UiField
	AppCellTable<Product> itemsTable;

	@UiField
	TableRowElement invoiceSubtotalGroup, invoiceTaxGroup,
			invoiceShippingGroup, invoiceDiscountGroup;

	@UiField
	TableCellElement invoiceTaxLabel, invoiceDiscountLabel;

	@UiField
	TableCellElement invoiceSubtotalValue, invoiceTaxValue,
			invoiceShippingValue, invoiceDiscountValue, invoiceTotalValue;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;

	private InvoicesConstants invoicesConstants = GWT
			.create(InvoicesConstants.class);

	private ProductsConstants productsConstants = GWT
			.create(ProductsConstants.class);

	private CustomersConstants customersConstants = GWT
			.create(CustomersConstants.class);

	/**
	 * Constructor
	 */
	public InvoicesViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		// hide by default
		tablePanel.setVisible(false);
		formPanel.setVisible(false);

		createInvoicesTable();
		createCustomersTable();
		createItemsInvoicesTable();
		createProductsTable();
	}

	/**
	 * Create celltable columns
	 */
	private void createInvoicesTable() {
		// /////////////////////////////////////////////////////////////////////
		// NAME COLUMN
		TextColumn<Invoice> idColumn = new TextColumn<Invoice>() {

			@Override
			public String getValue(Invoice object) {
				// TODO Auto-generated method stub
				return object.getId().toString();
			}
		};
		invoicesTable.addColumn(idColumn, invoicesConstants.columnId());

		// Make column sortable.
		idColumn.setSortable(true);
		invoicesTable.setComparator(idColumn, new Comparator<Invoice>() {
			public int compare(Invoice o1, Invoice o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		invoicesTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Invoice> selectedSet = invoicesTable.getSelectedSet();

				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteTableButton.setVisible(visible);

				if (invoicesTable.getSelected() != null) {
					String id = invoicesTable.getSelected().getId().toString();
					presenter.goTo(new InvoicesPlace("edit/" + id));
				}
			}
		});

	}

	private void createCustomersTable() {
		// /////////////////////////////////////////////////////////////////////
		// CUSTOMERS NAME COLUMN
		TextColumn<Customer> nameColumn = new TextColumn<Customer>() {

			@Override
			public String getValue(Customer object) {
				return object.getName();
			}
		};
		customersTable.addColumn(nameColumn, customersConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// CUSTOMERS EMAIL COLUMN
		TextColumn<Customer> emailColumn = new TextColumn<Customer>() {

			@Override
			public String getValue(Customer object) {
				return object.getEmail();
			}
		};
		customersTable.addColumn(emailColumn, customersConstants.columnEmail());

		// /////////////////////////////////////////////////////////////////////
		// CUSTOMERS CELLTABLE CLICKHANDLER
		customersTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (customersTable.getSelected() != null) {
					JavaScriptUtils.modal("selectCustomerModal", "hide");
					customerName.setValue(customersTable.getSelected()
							.getName());
					customerField = customersTable.getSelected();
				}
			}
		});
	}

	private void createItemsInvoicesTable() {
		itemsInvoicesTable.setStyleName("table table-condensed");

		// --------------------------------------------------------------------
		// PRODUCTS INVOICES DESCRIPTION COLUMN
		ColumnFlexTable<Product> checkboxColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				final CheckBox checkbox = new CheckBox();

				checkbox.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						boolean checked = false;

						for (int i = 1; i < itemsInvoicesTable.getRowCount(); i++) {
							CheckBox checkBox = (CheckBox) itemsInvoicesTable
									.getWidget(i, 0);

							if (checkBox.getValue())
								checked = true;
						}

						deleteProductButton.setVisible(checked);

					}
				});

				return checkbox;
			}
		};

		checkboxProductsInvoicesHeader = new CheckBox();

		checkboxProductsInvoicesHeader.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = checkboxProductsInvoicesHeader.getValue();

				for (int i = 1; i < itemsInvoicesTable.getRowCount(); i++) {
					CheckBox c = (CheckBox) itemsInvoicesTable.getWidget(i, 0);

					c.setValue(checked);
				}

				deleteProductButton.setVisible(checked);

			}
		});

		itemsInvoicesTable.addColumn(checkboxColumn,
				checkboxProductsInvoicesHeader);
		itemsInvoicesTable.setColumnWidth(checkboxColumn, "1%");

		// --------------------------------------------------------------------
		// PRODUCTS INVOICES DESCRIPTION COLUMN
		ColumnFlexTable<Product> quantityColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				AppDoubleBox quantity = new AppDoubleBox();
				quantity.setValue(1d);

				quantity.addChangeHandler(new ChangeHandler() {

					@Override
					public void onChange(ChangeEvent event) {
						updateTotals();

					}
				});

				return quantity;
			}
		};
		quantityColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		itemsInvoicesTable.addColumn(quantityColumn, new Label("Quantity"));
		itemsInvoicesTable.setColumnWidth(quantityColumn, "10%");

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES DESCRIPTION COLUMN
		ColumnFlexTable<Product> descriptionColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				AppTextArea textArea = new AppTextArea();

				textArea.setStyleName("expand50-200 input-block-level");
				textArea.setValue(object.getDescription());

				return textArea;
			}
		};
		descriptionColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		itemsInvoicesTable.addColumn(descriptionColumn, new Label(
				productsConstants.columnDescription()));
		itemsInvoicesTable.setColumnWidth(descriptionColumn, "69%");

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES PRICE COLUMN
		ColumnFlexTable<Product> priceColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				AppDoubleBox price = new AppDoubleBox();
				price.setValue(object.getPrice());

				price.addChangeHandler(new ChangeHandler() {

					@Override
					public void onChange(ChangeEvent event) {
						updateTotals();

					}
				});

				return price;
			}
		};
		priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		itemsInvoicesTable.addColumn(priceColumn,
				new Label(productsConstants.columnPrice()));
		itemsInvoicesTable.setColumnWidth(priceColumn, "10%");

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES PRICE COLUMN
		ColumnFlexTable<Product> totalColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				String price = NumberFormat.getFormat("#,##0.00").format(
						object.getPrice());

				return new Label(price);
			}
		};
		totalColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		itemsInvoicesTable.addColumn(totalColumn,
				new Label(productsConstants.columnPrice()));
		itemsInvoicesTable.setColumnWidth(totalColumn, "10%");
	}

	private void createProductsTable() {
		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS NAME COLUMN
		TextColumn<Product> productsNameColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getName();
			}
		};
		itemsTable.addColumn(productsNameColumn,
				productsConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS DESCRIPTION COLUMN
		TextColumn<Product> descriptionColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getDescription();
			}
		};
		itemsTable.addColumn(descriptionColumn,
				productsConstants.columnDescription());

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS PRICE COLUMN
		Column<Product, Number> priceColumn = new Column<Product, Number>(
				new NumberCell(NumberFormat.getFormat("#,##0.00"))) {
			@Override
			public Number getValue(Product object) {
				return object.getPrice();
			}
		};
		priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		itemsTable.addColumn(priceColumn, productsConstants.columnPrice());
		itemsTable.setColumnWidth(priceColumn, "6em");
		// Make the first name column sortable.
		priceColumn.setSortable(true);
		itemsTable.setComparator(priceColumn, new Comparator<Product>() {
			public int compare(Product o1, Product o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

	}

	/**
	 * Set list data to cellTable
	 * 
	 * @param list
	 */
	public void setList(List<Invoice> data) {
		invoicesTable.setList(data);
	}

	@Override
	public void setListProduct(List<Product> data) {
		itemsTable.setList(data);

	}

	@Override
	public void setListCustomer(List<Customer> data) {
		customersTable.setList(data);

		for (Customer customer : data) {
			customerSuggestions.add(customer.getName());
		}

	}

	/**
	 * UIHANDLERS
	 * 
	 */
	@UiHandler("deleteTableButton")
	protected void onClickDeleteTableButton(ClickEvent event) {
		if (Window.confirm(appConstants.areYouSure())) {
			presenter.doDelete(invoicesTable.getSelectedSet());
		}
	}

	@UiHandler("validationButton")
	protected void onClickValidationButton(ClickEvent event) {
		event.preventDefault();
		boolean visible = true;

		if (validationPanel.isVisible())
			visible = false;

		validationPanel.setVisible(visible);
	}

	@UiHandler("addNewLine")
	protected void onClickAddNewLine(ClickEvent event) {
		Product product = new Product();

		itemsInvoicesTable.add(product);
	}

	@UiHandler("selectCustomerButton")
	protected void onClickSelectCustomerButton(ClickEvent event) {
		customersTable.clearSelected();
		JavaScriptUtils.modal("selectCustomerModal", "show");
	}

	@UiHandler("selectCustomerModalCancel")
	protected void onCLickSelectCustomerModalCancel(ClickEvent event) {
		JavaScriptUtils.modal("selectCustomerModal", "hide");
	}

	@UiHandler("submitButton")
	protected void onClickSubmitButton(ClickEvent event) {
		event.preventDefault();
		formPanel.submit();
	}

	@UiHandler("formPanel")
	protected void onSubmitFormPanel(SubmitEvent event) {
		event.cancel();
		clearErrors();
		presenter.doSave();
	}

	@UiHandler("deleteProductButton")
	protected void onClickDeleteProductButton(ClickEvent event) {
		for (int i = itemsInvoicesTable.getRowCount() - 1; i > 0; i--) {
			CheckBox checkBox = (CheckBox) itemsInvoicesTable.getWidget(i, 0);

			if (checkBox.getValue()) {
				itemsInvoicesTable.removeRow(i);
			}
		}

		deleteProductButton.setVisible(false);
		checkboxProductsInvoicesHeader.setValue(false);

		updateTotals();
	}

	@UiHandler("addTableButton")
	protected void onClickAddTableButton(ClickEvent event) {
		presenter.goTo(new InvoicesPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickBackButton(ClickEvent event) {
		presenter.goTo(new InvoicesPlace());
	}

	@UiHandler("toolbarRefreshButton")
	protected void onCLickToolbarRefreshButton(ClickEvent event) {
		presenter.doLoad();
	}

	@UiHandler("formDeleteButton")
	protected void onCLickFormDeleteButton(ClickEvent event) {
		if (Window.confirm(appConstants.areYouSure()))
			presenter.doDelete();
	}

	@UiHandler("addItemButton")
	protected void onClickAddProductButton(ClickEvent event) {
		itemsTable.clearSelected();
		JavaScriptUtils.modal("selectItemsModal", "show");
	}

	@UiHandler("selectItemsModalCancel")
	protected void onClickSelectItemsModalCancel(ClickEvent event) {
		JavaScriptUtils.modal("selectItemsModal", "hide");

	}

	@UiHandler("selectItemsModalOk")
	protected void onClickSelectItemsModalOk(ClickEvent event) {
		JavaScriptUtils.modal("selectItemsModal", "hide");

		Set<Product> selected = itemsTable.getSelectedSet();
		itemsTable.clearSelected();

		if (selected.size() > 0) {
			for (Product p : selected) {
				itemsInvoicesTable.add(p);
			}

		}

		updateTotals();

	}

	@UiHandler("customerName")
	protected void onKeyPressCustomerName(KeyPressEvent event) {

		int key = event.getNativeEvent().getKeyCode();
		if (key == KeyCodes.KEY_ENTER) {
			event.stopPropagation();
			event.getNativeEvent().preventDefault();
		}
	}

	@UiHandler(value = { "discount", "tax", "shipping" })
	protected void onChangeDiscount(ChangeEvent event) {
		updateTotals();
	}

	/**
	 * Update invoice totals
	 */
	private void updateTotals() {
		Double subTotal = 0d;

		for (int i = 1; i < itemsInvoicesTable.getRowCount(); i++) {
			AppDoubleBox qtyWidget = (AppDoubleBox) itemsInvoicesTable
					.getWidget(i, 1);
			AppDoubleBox priceWidget = (AppDoubleBox) itemsInvoicesTable
					.getWidget(i, 3);
			Label totalWidget = (Label) itemsInvoicesTable.getWidget(i, 4);

			Double total = qtyWidget.getValue() * priceWidget.getValue();

			subTotal += total;

			totalWidget.setText(NumberFormat.getFormat("#,##0.00")
					.format(total));

		}

		// update subtotal
		String displaySubtotal = "display:none";
		if (tax.getValue() > 0 || discount.getValue() > 0
				|| shipping.getValue() > 0)
			displaySubtotal = "";

		invoiceSubtotalValue.setInnerText(NumberFormat.getFormat("#,##0.00")
				.format(subTotal));
		invoiceSubtotalGroup.setAttribute("style", displaySubtotal);

		// update tax
		String displayTax = "display:none";
		Double taxTotal = 0d;
		if (tax.getValue() > 0) {
			displayTax = "";

			taxTotal = subTotal * tax.getValue() / 100;

			invoiceTaxLabel.setInnerText(invoicesConstants.invoiceTax() + " ("
					+ tax.getValue() + "%)");
			invoiceTaxValue.setInnerText(NumberFormat.getFormat("#,##0.00")
					.format(taxTotal));
		}

		invoiceTaxGroup.setAttribute("style", displayTax);

		// update discount
		String displayDiscount = "display:none";
		Double discountTotal = 0d;
		if (discount.getValue() > 0) {
			displayDiscount = "";

			discountTotal = subTotal * discount.getValue() / 100;

			invoiceDiscountLabel.setInnerText(invoicesConstants
					.invoiceDiscount() + " (" + discount.getValue() + "%)");
			invoiceDiscountValue.setInnerText(NumberFormat
					.getFormat("#,##0.00").format(discountTotal));
		}
		invoiceDiscountGroup.setAttribute("style", displayDiscount);

		// update shipping
		String displayShipping = "display:none";
		if (shipping.getValue() > 0)
			displayShipping = "";

		invoiceShippingValue.setInnerText(shipping.getValueAsText());
		invoiceShippingGroup.setAttribute("style", displayShipping);

		// update total
		Double total = subTotal + taxTotal - discountTotal
				+ shipping.getValue();
		invoiceTotalValue.setInnerText(NumberFormat.getFormat("#,##0.00")
				.format(total));
	}

	@Override
	public void setFormDeleteButtonVisible(boolean visible) {
		formDeleteButton.setVisible(visible);

	}

	/**
	 * Show form errors
	 * 
	 * @param error
	 */
	public void setFormErrors(String error) {
		validationButton.setVisible(true);
		validationPanel.getElement().setInnerHTML(error);
	}

	/**
	 * Show form fields error
	 * 
	 * @param field
	 * @param error
	 */
	public void setFieldError(String field, String error) {
		if (field.equals("notes"))
			notesCG.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		notesCG.setClassName("control-group");

		validationButton.setVisible(false);
		validationPanel.setVisible(false);
	}

	/**
	 * Create a editor driver
	 */
	@Override
	public SimpleBeanEditorDriver<Invoice, ?> createEditorDriver() {
		Driver driver = GWT.create(Driver.class);
		driver.initialize(this);

		return driver;
	}

	/**
	 * Show form panel
	 */
	@Override
	public void showFormPanel(String legendText) {
		legend.setText(legendText);

		clearErrors();

		tablePanel.setVisible(false);
		formPanel.setVisible(true);

		deleteProductButton.setVisible(false);
		checkboxProductsInvoicesHeader.setValue(false);

		updateTotals();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				customerName.setFocus(true);

			}
		});

	}

	/**
	 * Show CellTable panel
	 */
	@Override
	public void showCellTablePanel() {
		tablePanel.setVisible(true);
		formPanel.setVisible(false);

		deleteTableButton.setVisible(false);

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}
