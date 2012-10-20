package cbmarc.cigbill.client.main.invoices;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.products.ProductsConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.utils.IFilter;
import cbmarc.cigbill.shared.Invoice;
import cbmarc.cigbill.shared.Product;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
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

	@UiField(provided = true)
	AppCellTable<Invoice> cellTable = new AppCellTable<Invoice>(
			new IFilter<Invoice>() {
				@Override
				public boolean isValid(Invoice value, String filter) {
					if (filter == null || value == null)
						return true;
					return true;
				}
			});

	@Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel cellTablePanel;
	@UiField
	Button addTableButton, deleteTableButton, toolbarRefreshButton;

	// Validatior error messages
	@UiField
	Anchor validationAnchor;
	@UiField
	FocusPanel validationPanel;

	// Form fields
	@UiField
	FormPanel formPanel;
	@UiField
	HTMLPanel productsInvoicesPanel, productsPanel;
	@UiField(provided = true)
	AppCellTable<Product> productsInvoicesCellTable = new AppCellTable<Product>(
			new IFilter<Product>() {

				@Override
				public boolean isValid(Product value, String filter) {
					// TODO Auto-generated method stub
					return true;
				}
			});
	@UiField(provided = true)
	AppCellTable<Product> productsCellTable = new AppCellTable<Product>(
			new IFilter<Product>() {

				@Override
				public boolean isValid(Product value, String filter) {
					// TODO Auto-generated method stub
					return true;
				}
			});
	@UiField
	Button addProductButton, deleteProductButton, returnProductButton;
	@UiField
	TextArea notes;
	@UiField
	SubmitButton submitButton;
	@UiField
	Button backButton, formDeleteButton;

	// Control groups for mark errors
	@UiField
	DivElement notesCG;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;
	private InvoicesConstants invoicesConstants = GWT
			.create(InvoicesConstants.class);
	private ProductsConstants productsConstants = GWT
			.create(ProductsConstants.class);

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
		cellTable.getCellTable().addColumn(idColumn,
				invoicesConstants.columnId());

		// Make column sortable.
		idColumn.setSortable(true);
		cellTable.getListHandler().setComparator(idColumn,
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
				Set<Invoice> selectedSet = cellTable.getSelectionModel()
						.getSelectedSet();

				deleteTableButton.setVisible(selectedSet.size() > 0 ? true
						: false);
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
		productsInvoicesCellTable.getCellTable().addColumn(nameColumn,
				productsConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS INVOICES DESCRIPTION COLUMN
		TextColumn<Product> descriptionColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getDescription();
			}
		};
		productsInvoicesCellTable.getCellTable().addColumn(descriptionColumn,
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
		productsInvoicesCellTable.getCellTable().addColumn(priceColumn,
				productsConstants.columnPrice());
		productsInvoicesCellTable.getCellTable().setColumnWidth(priceColumn,
				6.0, Unit.EM);
		// Make the first name column sortable.
		priceColumn.setSortable(true);
		productsInvoicesCellTable.getListHandler().setComparator(priceColumn,
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
						.getSelectionModel().getSelectedSet();

				deleteProductButton.setVisible(selectedSet.size() > 0 ? true
						: false);
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
		productsCellTable.getCellTable().addColumn(productsNameColumn,
				productsConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS DESCRIPTION COLUMN
		TextColumn<Product> descriptionColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getDescription();
			}
		};
		productsCellTable.getCellTable().addColumn(descriptionColumn,
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
		productsCellTable.getCellTable().addColumn(priceColumn,
				productsConstants.columnPrice());
		productsCellTable.getCellTable().setColumnWidth(priceColumn,
				6.0, Unit.EM);
		// Make the first name column sortable.
		priceColumn.setSortable(true);
		productsCellTable.getListHandler().setComparator(priceColumn,
				new Comparator<Product>() {
					public int compare(Product o1, Product o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		// /////////////////////////////////////////////////////////////////////
		// PRODUCTS CELLTABLE CLICKHANDLER
		productsCellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Product> selected = productsCellTable.getSelectionModel()
						.getSelectedSet();
				productsCellTable.getSelectionModel().clear();

				if (selected.size() > 0) {
					List<Product> list = productsInvoicesCellTable
							.getDataProvider().getList();
					list.addAll(selected);

					productsInvoicesPanel.setVisible(true);
					productsPanel.setVisible(false);

					// Its ugly but it works
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {

						@Override
						public void execute() {
							productsInvoicesCellTable.getCellTable()
									.setVisibleRangeAndClearData(
											productsInvoicesCellTable
													.getCellTable()
													.getVisibleRange(), true);

						}
					});
				}
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

		// sort descending
		cellTable.getCellTable().getColumnSortList().clear();
		cellTable.getCellTable().getColumnSortList().push(idColumn);
		cellTable.getCellTable().getColumnSortList().push(idColumn);
	}

	@Override
	public void setListProduct(List<Product> data) {
		productsCellTable.setList(data);
		cellTable.getCellTable().getColumnSortList().push(productsNameColumn);

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
			Set<Invoice> items = cellTable.getSelectionModel().getSelectedSet();
			presenter.doDelete(items);
		}
	}

	@UiHandler(value = { "validationPanel", "validationAnchor" })
	protected void onClickValidation(ClickEvent event) {
		event.preventDefault();
		boolean visible = true;

		if (validationPanel.isVisible())
			visible = false;

		validationPanel.setVisible(visible);
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
		productsInvoicesPanel.setVisible(false);
		productsPanel.setVisible(true);
	}

	@UiHandler("returnProductButton")
	protected void onClickReturnProductButton(ClickEvent event) {
		productsInvoicesPanel.setVisible(true);
		productsPanel.setVisible(false);

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
		validationAnchor.setVisible(true);
		validationPanel.getElement().setInnerHTML(error);
	}

	/**
	 * Show form fields error
	 * 
	 * @param field
	 * @param error
	 */
	public void setFieldError(String field, String error) {
		DivElement divElement = null;

		if (field.equals("notes"))
			divElement = notesCG;

		if (divElement != null)
			divElement.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		String styleName = "control-group";

		notesCG.setClassName(styleName);

		validationAnchor.setVisible(false);
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
		productsInvoicesPanel.setVisible(true);
		productsPanel.setVisible(false);
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

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				cellTable.setFocus();

			}
		});

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}
