package cbmarc.cigbill.client.main.invoices;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.customers.CustomersConstants;
import cbmarc.cigbill.client.main.products.ProductsConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.utils.IFilter;
import cbmarc.cigbill.shared.Customer;
import cbmarc.cigbill.shared.Invoice;
import cbmarc.cigbill.shared.Product;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.SuggestBox;
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
	AppCellTable<Invoice> cellTable;
	
	@UiField
	WellForm formPanel;

	@Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel cellTablePanel, productsInvoicesPanel;
	
	@UiField
	TextArea notes;
	
	@UiField
	AlertBlock validationPanel;

	@UiField
	Modal selectCustomerModal, selectProductsModal;
	
	@UiField
	ControlGroup notesCG;

	@UiField
	Button addTableButton, deleteTableButton, toolbarRefreshButton,
			selectCustomerModalCancel, validationButton, selectCustomerButton,
			addProductButton, deleteProductButton, selectProductsModalOk,
			selectProductsModalCancel, backButton, formDeleteButton;
	
	@UiField
	SubmitButton submitButton;

	MultiWordSuggestOracle customerSuggestions = new MultiWordSuggestOracle();
	@Editor.Ignore
	@UiField(provided = true)
	SuggestBox customerName = new SuggestBox(customerSuggestions);
	@UiField
	AppCellTable<Customer> customersCellTable;

	@UiField
	AppCellTable<Product> productsInvoicesCellTable;
	@UiField
	AppCellTable<Product> productsCellTable;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;
	
	private InvoicesConstants invoicesConstants = GWT
			.create(InvoicesConstants.class);
	private ProductsConstants productsConstants = GWT
			.create(ProductsConstants.class);
	private CustomersConstants customersConstants = GWT
			.create(CustomersConstants.class);

	Column<Invoice, SafeHtml> idColumn;
	TextColumn<Product> productsNameColumn;

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
	}

	/**
	 * Create celltable columns
	 */
	private void createInvoicesCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// NAME COLUMN
		idColumn = new Column<Invoice, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Invoice object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				Anchor anchor = new Anchor(object.getId().toString());
				anchor.setHref("#invoices:edit/" + object.getId().toString());

				sb.appendHtmlConstant(anchor.toString());

				return sb.toSafeHtml();
			}
		};
		cellTable.addColumn(idColumn,
				invoicesConstants.columnId());

		// Make column sortable.
		idColumn.setSortable(true);
		cellTable.setComparator(idColumn,
				new Comparator<Invoice>() {
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
					selectCustomerModal.hide();
					Window.alert("SELECTED");
					//taxName.setValue(taxCellTable.getSelected().getName());
				}
			}
		});
	}

	private void createInvoicesProductsCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES NAME COLUMN
		TextColumn<Product> nameColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getName();
			}
		};
		productsInvoicesCellTable.addColumn(nameColumn,
				productsConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES DESCRIPTION COLUMN
		TextColumn<Product> descriptionColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getDescription();
			}
		};
		productsInvoicesCellTable.addColumn(descriptionColumn,
				productsConstants.columnDescription());

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES PRICE COLUMN
		Column<Product, Number> priceColumn = new Column<Product, Number>(
				new NumberCell(NumberFormat.getFormat("#,##0.00"))) {
			@Override
			public Number getValue(Product object) {
				return object.getPrice();
			}
		};
		priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		productsInvoicesCellTable.addColumn(priceColumn,
				productsConstants.columnPrice());
		productsInvoicesCellTable.setColumnWidth(priceColumn,
				"6em");
		// Make the first name column sortable.
		priceColumn.setSortable(true);
		productsInvoicesCellTable.setComparator(priceColumn,
				new Comparator<Product>() {
					public int compare(Product o1, Product o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES CELLTABLE CLICKHANDLER
		productsInvoicesCellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Product> selectedSet = productsInvoicesCellTable
						.getSelectedSet();

				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteProductButton.setVisible(visible);
			}
		});
	}

	private void createProductsCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS NAME COLUMN
		productsNameColumn = new TextColumn<Product>() {

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
		productsCellTable.setComparator(priceColumn,
				new Comparator<Product>() {
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
		//cellTable.getCellTable().getColumnSortList().clear();
		//cellTable.getCellTable().getColumnSortList().push(idColumn);
		//cellTable.getCellTable().getColumnSortList().push(idColumn);
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

	@UiHandler("selectCustomerButton")
	protected void onClickSelectCustomerButton(ClickEvent event) {
		customersCellTable.clearSelected();
		selectCustomerModal.show();
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
		selectProductsModal.show();
	}

	@UiHandler("selectProductsModalCancel")
	protected void onClickSelectProductsModalCancel(ClickEvent event) {
		selectProductsModal.hide();

	}

	@UiHandler("selectProductsModalOk")
	protected void onClickSelectProductsModalOk(ClickEvent event) {
		selectProductsModal.hide();

		Set<Product> selected = productsCellTable.getSelectedSet();
		productsCellTable.clearSelected();

		if (selected.size() > 0) {
			/*List<Product> list = productsInvoicesCellTable.getDataProvider()
					.getList();
			list.addAll(selected);

			// Its ugly but it works
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {

				@Override
				public void execute() {
					productsInvoicesCellTable.getCellTable()
							.setVisibleRangeAndClearData(
									productsInvoicesCellTable.getCellTable()
											.getVisibleRange(), true);

				}
			});*/
		}

	}

	@Override
	public Button getFormDeleteButton() {
		return formDeleteButton;
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
			notesCG.setType(ControlGroupType.ERROR);
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		notesCG.setType(ControlGroupType.NONE);

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
