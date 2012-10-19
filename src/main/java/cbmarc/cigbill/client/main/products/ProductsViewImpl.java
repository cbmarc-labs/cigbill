package cbmarc.cigbill.client.main.products;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.utils.IFilter;
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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
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
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ProductsViewImpl extends Composite implements ProductsView,
		Editor<Product> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ProductsViewImpl> {
	}

	public interface Driver extends
			SimpleBeanEditorDriver<Product, ProductsViewImpl> {
	}

	@UiField(provided = true)
	AppCellTable<Product> cellTable = new AppCellTable<Product>(
			new IFilter<Product>() {
				@Override
				public boolean isValid(Product value, String filter) {
					if (filter == null || value == null)
						return true;
					return value.getName().toLowerCase()
							.contains(filter.toLowerCase())
							|| value.getDescription().contains(
									filter.toLowerCase());
				}
			});

	@Editor.Ignore
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
	TextBox name, description;
	@UiField
	DoubleBox price;
	@UiField
	TextArea notes;
	@UiField
	SubmitButton submitButton;
	@UiField
	Button backButton, formDeleteButton;

	// Control groups for mark errors
	@UiField
	DivElement nameCG, descriptionCG, priceCG;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;
	private ProductsConstants productsConstants = GWT
			.create(ProductsConstants.class);

	Column<Product, SafeHtml> nameColumn;

	/**
	 * Constructor
	 */
	public ProductsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		// hide by default
		cellTablePanel.setVisible(false);
		formPanel.setVisible(false);

		createCellTable();
	}

	/**
	 * Create celltable columns
	 */
	private void createCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// NAME COLUMN
		nameColumn = new Column<Product, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Product object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				Anchor anchor = new Anchor(object.getName());
				anchor.setHref("#products:edit/" + object.getId());

				sb.appendHtmlConstant(anchor.toString());

				return sb.toSafeHtml();
			}
		};
		cellTable.getCellTable().addColumn(nameColumn,
				productsConstants.columnName());;
		cellTable.getCellTable().setColumnWidth(nameColumn, 16.0, Unit.EM);

		// Make the first name column sortable.
		nameColumn.setSortable(true);
		cellTable.getListHandler().setComparator(nameColumn,
				new Comparator<Product>() {
					public int compare(Product o1, Product o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		// /////////////////////////////////////////////////////////////////////
		// DESCRIPTION COLUMN
		TextColumn<Product> descriptionColumn = new TextColumn<Product>() {

			@Override
			public String getValue(Product object) {
				return object.getDescription();
			}
		};
		cellTable.getCellTable().addColumn(descriptionColumn,
				productsConstants.columnDescription());

		// /////////////////////////////////////////////////////////////////////
		// PRICE COLUMN
		Column<Product, Number> priceColumn = new Column<Product, Number>(
				//new NumberCell(NumberFormat.getFormat("+#,##0.00;-#,##0.00"))) {
				new NumberCell(NumberFormat.getFormat("#,##0.00"))) {
			@Override
			public Number getValue(Product object) {
				return object.getPrice();
			}
		};
		priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		cellTable.getCellTable().addColumn(priceColumn,
				productsConstants.columnPrice());
		cellTable.getCellTable().setColumnWidth(priceColumn, 6.0, Unit.EM);
		// Make the first name column sortable.
		priceColumn.setSortable(true);
		cellTable.getListHandler().setComparator(priceColumn,
				new Comparator<Product>() {
					public int compare(Product o1, Product o2) {
						return o1.getPrice().compareTo(o2.getPrice());
					}
				});

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Product> selectedSet = cellTable.getSelectionModel()
						.getSelectedSet();

				deleteTableButton.setVisible(selectedSet.size() > 0 ? true
						: false);
			}
		});

	}

	/**
	 * Set list data to cellTable
	 * 
	 * @param list
	 */
	public void setList(List<Product> list) {
		cellTable.setList(list);
		cellTable.getCellTable().getColumnSortList().clear();
		cellTable.getCellTable().getColumnSortList().push(nameColumn);
	}

	// only digits as input in text field

	@UiHandler("price")
	public void onKeyPress(KeyPressEvent event) {
		if (KeyCodes.KEY_ENTER != event.getCharCode()) {
			if (!"0123456789,.".contains(String.valueOf(event.getCharCode()))) {
				price.cancelKey();
			}
		}
	}

	@UiHandler("deleteTableButton")
	protected void onClickDeleteTableButton(ClickEvent event) {
		if (Window.confirm(appConstants.areYouSure())) {
			Set<Product> items = cellTable.getSelectionModel().getSelectedSet();
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
		presenter.goTo(new ProductsPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
		presenter.goTo(new ProductsPlace());
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

		if (field.equals("name"))
			divElement = nameCG;

		else if (field.equals("description"))
			divElement = descriptionCG;

		else if (field.equals("price"))
			divElement = priceCG;

		if (divElement != null)
			divElement.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		String styleName = "control-group";

		nameCG.setClassName(styleName);
		descriptionCG.setClassName(styleName);
		priceCG.setClassName(styleName);

		validationAnchor.setVisible(false);
		validationPanel.setVisible(false);
	}

	/**
	 * Create a editor driver
	 */
	@Override
	public SimpleBeanEditorDriver<Product, ?> createEditorDriver() {
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

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				name.setFocus(true);

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
