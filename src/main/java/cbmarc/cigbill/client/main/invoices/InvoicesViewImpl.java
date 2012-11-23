package cbmarc.cigbill.client.main.invoices;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.customers.CustomersConstants;
import cbmarc.cigbill.client.main.products.ProductsConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.ui.AppCheckboxCellTable;
import cbmarc.cigbill.client.ui.AppFlexTable;
import cbmarc.cigbill.client.ui.AppTextArea;
import cbmarc.cigbill.client.ui.ColumnFlexTable;
import cbmarc.cigbill.client.utils.JavaScriptUtils;
import cbmarc.cigbill.shared.Customer;
import cbmarc.cigbill.shared.Invoice;
import cbmarc.cigbill.shared.Product;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
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
	AppCheckboxCellTable<Invoice> cellTable;

	@UiField
	FormPanel formPanel;

	@Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel cellTablePanel, productsInvoicesPanel;

	@UiField
	TextArea notes;

	@UiField
	HTMLPanel validationPanel;

	@UiField
	DivElement notesCG;

	@UiField
	Button addTableButton, deleteTableButton, toolbarRefreshButton,
			selectCustomerModalCancel, validationButton, selectCustomerButton,
			addProductButton, deleteProductButton, selectProductsModalOk,
			selectProductsModalCancel, backButton, formDeleteButton,
			addNewLine;

	@UiField
	SubmitButton submitButton;

	MultiWordSuggestOracle customerSuggestions = new MultiWordSuggestOracle();

	@Editor.Ignore
	@UiField(provided = true)
	SuggestBox customerName = new SuggestBox(customerSuggestions);

	@UiField
	AppCellTable<Customer> customersCellTable;

	@UiField
	AppFlexTable<Product> productsInvoicesCellTable;

	@UiField
	AppCellTable<Product> productsCellTable;

	Customer customer;

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
		cellTablePanel.setVisible(false);
		formPanel.setVisible(false);

		createInvoicesCellTable();
		createCustomersCellTable();
		createInvoicesProductsCellTable();
		createProductsCellTable();

		// disable form submittion on this control
		customerName.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				int key = event.getNativeEvent().getKeyCode();
				if (key == KeyCodes.KEY_ENTER) {
					event.stopPropagation();
					event.getNativeEvent().preventDefault();
				}

			}
		});

	}

	/**
	 * Create celltable columns
	 */
	private void createInvoicesCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// NAME COLUMN
		TextColumn<Invoice> idColumn = new TextColumn<Invoice>(){

			@Override
			public String getValue(Invoice object) {
				// TODO Auto-generated method stub
				return object.getId().toString();
			}};
		cellTable.addColumn(idColumn, invoicesConstants.columnId());

		// Make column sortable.
		idColumn.setSortable(true);
		cellTable.setComparator(idColumn, new Comparator<Invoice>() {
			public int compare(Invoice o1, Invoice o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Invoice> selectedSet = cellTable.getSelectedSet();

				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteTableButton.setVisible(visible);

				if (cellTable.getSelected() != null) {
					String id = cellTable.getSelected().getId().toString();
					presenter.goTo(new InvoicesPlace("edit/" + id));
				}
			}
		});

	}

	private void createCustomersCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// CUSTOMERS NAME COLUMN
		TextColumn<Customer> nameColumn = new TextColumn<Customer>() {

			@Override
			public String getValue(Customer object) {
				return object.getName();
			}
		};
		customersCellTable.addColumn(nameColumn,
				customersConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// CUSTOMERS EMAIL COLUMN
		TextColumn<Customer> emailColumn = new TextColumn<Customer>() {

			@Override
			public String getValue(Customer object) {
				return object.getEmail();
			}
		};
		customersCellTable.addColumn(emailColumn,
				customersConstants.columnEmail());

		// /////////////////////////////////////////////////////////////////////
		// CUSTOMERS CELLTABLE CLICKHANDLER
		customersCellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (customersCellTable.getSelected() != null) {
					JavaScriptUtils.modal("selectCustomerModal", "hide");
					customerName.setValue(customersCellTable.getSelected()
							.getName());
					customer = customersCellTable.getSelected();
				}
			}
		});
	}

	private void createInvoicesProductsCellTable() {
		productsInvoicesCellTable.setStyleName("table table-condensed");

		// --------------------------------------------------------------------
		// PRODUCTS INVOICES DESCRIPTION COLUMN
		ColumnFlexTable<Product> checkboxColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				CheckBox checkbox = new CheckBox();

				checkbox.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						updateInvoicesProductCellTable();

					}
				});

				return checkbox;
			}
		};

		final CheckBox checkbox = new CheckBox();

		checkbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = checkbox.getValue();

				for (int i = 1; i < productsInvoicesCellTable.getRowCount(); i++) {
					CheckBox c = (CheckBox) productsInvoicesCellTable
							.getWidget(i, 0);

					c.setValue(checked);
				}

				deleteProductButton.setVisible(checked);

			}
		});

		productsInvoicesCellTable.addColumn(checkboxColumn, checkbox);
		productsInvoicesCellTable.setColumnWidth(checkboxColumn, "1%");

		// --------------------------------------------------------------------
		// PRODUCTS INVOICES DESCRIPTION COLUMN
		ColumnFlexTable<Product> quantityColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				TextBox textBox = new TextBox();

				textBox.setStyleName("input-mini");
				textBox.setAlignment(TextAlignment.RIGHT);
				textBox.setValue(object.getName());

				return textBox;
			}
		};
		quantityColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		productsInvoicesCellTable.addColumn(quantityColumn, new Label(
				"Quantity"));
		productsInvoicesCellTable.setColumnWidth(quantityColumn, "10%");

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

		productsInvoicesCellTable.addColumn(descriptionColumn, new Label(
				productsConstants.columnDescription()));
		productsInvoicesCellTable.setColumnWidth(descriptionColumn, "69%");

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES PRICE COLUMN
		ColumnFlexTable<Product> priceColumn = new ColumnFlexTable<Product>() {

			@Override
			public Widget getValue(Product object) {
				DoubleBox textBox = new DoubleBox();
				String price = NumberFormat.getFormat("#,##0.00").format(
						object.getPrice());

				textBox.setStyleName("input-mini");
				textBox.setAlignment(TextAlignment.RIGHT);
				textBox.setValue(object.getPrice());

				return textBox;
			}
		};
		priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		productsInvoicesCellTable.addColumn(priceColumn, new Label(
				productsConstants.columnPrice()));
		productsInvoicesCellTable.setColumnWidth(priceColumn, "10%");

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

		productsInvoicesCellTable.addColumn(totalColumn, new Label(
				productsConstants.columnPrice()));
		productsInvoicesCellTable.setColumnWidth(totalColumn, "10%");

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES CELLTABLE CLICKHANDLER
		productsInvoicesCellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});
	}

	private void updateInvoicesProductCellTable() {
		boolean visible = false;

		for (int i = 1; i < productsInvoicesCellTable.getRowCount(); i++) {
			CheckBox checkBox = (CheckBox) productsInvoicesCellTable.getWidget(
					i, 0);

			if (checkBox.getValue())
				visible = true;
		}

		deleteProductButton.setVisible(visible);
	}

	private void createProductsCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS NAME COLUMN
		TextColumn<Product> productsNameColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getName();
			}
		};
		productsCellTable.addColumn(productsNameColumn,
				productsConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS DESCRIPTION COLUMN
		TextColumn<Product> descriptionColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getDescription();
			}
		};
		productsCellTable.addColumn(descriptionColumn,
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
		productsCellTable.addColumn(priceColumn,
				productsConstants.columnPrice());
		productsCellTable.setColumnWidth(priceColumn, "6em");
		// Make the first name column sortable.
		priceColumn.setSortable(true);
		productsCellTable.setComparator(priceColumn, new Comparator<Product>() {
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
		cellTable.setList(data);

		// TODO sort descending
		// cellTable.getCellTable().getColumnSortList().clear();
		// cellTable.getCellTable().getColumnSortList().push(idColumn);
		// cellTable.getCellTable().getColumnSortList().push(idColumn);
	}

	@Override
	public void setListProduct(List<Product> data) {
		productsCellTable.setList(data);

	}

	@Override
	public void setListCustomer(List<Customer> data) {
		customersCellTable.setList(data);

		for (Customer customer : data) {
			customerSuggestions.add(customer.getName());
		}

	}

	/*
	 * // only digits as input in text field
	 * 
	 * @UiHandler("testBox") public void onKeyPress(KeyPressEvent event) { if
	 * (!"0123456789".contains(String.valueOf(event.getCharCode()))) {
	 * textBox.cancelKey(); } }
	 */

	@UiHandler("deleteTableButton")
	protected void onClickDeleteTableButton(ClickEvent event) {
		if (Window.confirm(appConstants.areYouSure())) {
			presenter.doDelete(cellTable.getSelectedSet());
		}
	}

	@UiHandler("validationButton")
	protected void onClickValidation(ClickEvent event) {
		event.preventDefault();
		boolean visible = true;

		if (validationPanel.isVisible())
			visible = false;

		validationPanel.setVisible(visible);
	}

	@UiHandler("addNewLine")
	protected void onClickAddNewLine(ClickEvent event) {
		Product product = new Product();

		productsInvoicesCellTable.add(product);
	}

	@UiHandler("selectCustomerButton")
	protected void onClickSelectCustomerButton(ClickEvent event) {
		customersCellTable.clearSelected();
		JavaScriptUtils.modal("selectCustomerModal", "show");
	}

	@UiHandler("submitButton")
	protected void onClickSubmitButton(ClickEvent event) {
		event.preventDefault();
		formPanel.submit();
	}

	@UiHandler("formPanel")
	protected void onSubmitformPanel(SubmitEvent event) {
		event.cancel();
		clearErrors();
		presenter.doSave();
	}

	@UiHandler("deleteProductButton")
	protected void onClickDeleteProductButton(ClickEvent event) {
		for (int i = 1; i < productsInvoicesCellTable.getRowCount(); i++) {
			CheckBox checkBox = (CheckBox) productsInvoicesCellTable.getWidget(
					i, 0);

			if (checkBox.getValue()) {
				productsInvoicesCellTable.removeRow(i);

				i--;
			}
		}

		deleteProductButton.setVisible(false);
	}

	@UiHandler("addTableButton")
	protected void onClickAddTableButton(ClickEvent event) {
		presenter.goTo(new InvoicesPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
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

	@UiHandler("addProductButton")
	protected void onClickAddProductButton(ClickEvent event) {
		productsCellTable.clearSelected();
		JavaScriptUtils.modal("selectProductsModal", "show");
	}

	@UiHandler("selectProductsModalCancel")
	protected void onClickSelectProductsModalCancel(ClickEvent event) {
		JavaScriptUtils.modal("selectProductsModal", "hide");

	}

	@UiHandler("selectProductsModalOk")
	protected void onClickSelectProductsModalOk(ClickEvent event) {
		JavaScriptUtils.modal("selectProductsModal", "hide");

		Set<Product> selected = productsCellTable.getSelectedSet();
		productsCellTable.clearSelected();

		if (selected.size() > 0) {

			// List<Product> list = productsInvoicesCellTable.getDataProvider()
			// .getList();
			// list.addAll(selected);

			// Its ugly but it works
			/*
			 * Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			 * 
			 * @Override public void execute() {
			 * productsInvoicesCellTable.getCellTable()
			 * .setVisibleRangeAndClearData(
			 * productsInvoicesCellTable.getCellTable() .getVisibleRange(),
			 * true);
			 * 
			 * } });
			 */

		}

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

		cellTablePanel.setVisible(false);
		formPanel.setVisible(true);
		deleteProductButton.setVisible(false);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				notes.setFocus(true);

			}
		});

	}

	/**
	 * Show CellTable panel
	 */
	@Override
	public void showCellTablePanel() {
		cellTablePanel.setVisible(true);
		formPanel.setVisible(false);

		deleteTableButton.setVisible(false);

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}
