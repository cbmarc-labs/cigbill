package cbmarc.cigbill.client.main.products;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.taxes.TaxesConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.shared.Product;
import cbmarc.cigbill.shared.Tax;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
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
public class ProductsViewImpl extends Composite implements ProductsView,
		Editor<Product> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ProductsViewImpl> {
	}

	public interface Driver extends
			SimpleBeanEditorDriver<Product, ProductsViewImpl> {
	}

	@UiField
	AppCellTable<Product> cellTable;

	@Editor.Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel cellTablePanel;

	@UiField
	Button addTableButton, deleteTableButton, toolbarRefreshButton;

	// Validatior error messages
	@UiField
	Button validationButton;

	@UiField
	AlertBlock validationPanel;

	// Form fields
	@UiField
	WellForm formPanel;

	@UiField
	TextBox name, description;

	@UiField
	DoubleBox price;

	MultiWordSuggestOracle taxSuggestions = new MultiWordSuggestOracle();

	@Editor.Ignore
	@UiField(provided = true)
	SuggestBox taxName = new SuggestBox(taxSuggestions);

	@UiField
	Button selectTaxButton;

	@UiField
	TextArea notes;

	@UiField
	SubmitButton submitButton;

	@UiField
	Button backButton, formDeleteButton;

	// Control groups for mark errors
	@UiField
	ControlGroup nameCG, descriptionCG, priceCG;

	@UiField
	Modal taxModal;

	@UiField
	AppCellTable<Tax> taxCellTable;

	@UiField
	Button taxModalCancelButton;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;
	private ProductsConstants productsConstants = GWT
			.create(ProductsConstants.class);
	private TaxesConstants taxesConstants = GWT.create(TaxesConstants.class);

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
		createTaxCellTable();
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
		cellTable.addColumn(nameColumn, productsConstants.columnName());
		cellTable.setColumnWidth(nameColumn, "16em");

		// Make the first name column sortable.

		nameColumn.setSortable(true);
		cellTable.setComparator(nameColumn, new Comparator<Product>() {
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
		cellTable.addColumn(descriptionColumn,
				productsConstants.columnDescription());

		// /////////////////////////////////////////////////////////////////////
		// PRICE COLUMN
		Column<Product, Number> priceColumn = new Column<Product, Number>(
		// new NumberCell(NumberFormat.getFormat("+#,##0.00;-#,##0.00"))) {
				new NumberCell(NumberFormat.getFormat("#,##0.00"))) {
			@Override
			public Number getValue(Product object) {
				return object.getPrice();
			}
		};
		priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		cellTable.addColumn(priceColumn, productsConstants.columnPrice());
		cellTable.setColumnWidth(priceColumn, "6em");
		
		// Make the first name column sortable.
		priceColumn.setSortable(true);

		cellTable.setComparator(priceColumn, new Comparator<Product>() {
			public int compare(Product o1, Product o2) {
				return o1.getPrice().compareTo(o2.getPrice());
			}
		});

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Product> selectedSet = cellTable.getSelectedSet();

				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteTableButton.setVisible(visible);
			}
		});

	}

	/**
	 * Create celltable columns
	 */
	private void createTaxCellTable() {
		// /////////////////////////////////////////////////////////////////////
		// NAME COLUMN
		TextColumn<Tax> nameColumn = new TextColumn<Tax>() {

			@Override
			public String getValue(Tax object) {
				return object.getName();
			}
		};
		taxCellTable.addColumn(nameColumn, taxesConstants.columnName());

		// /////////////////////////////////////////////////////////////////////
		// DESCRIPTION COLUMN
		TextColumn<Tax> descriptionColumn = new TextColumn<Tax>() {

			@Override
			public String getValue(Tax object) {
				return object.getDescription();
			}
		};
		taxCellTable.addColumn(descriptionColumn,
				taxesConstants.columnDescription());

		taxCellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (taxCellTable.getSelected() != null) {
					taxModal.hide();
					taxName.setValue(taxCellTable.getSelected().getName());
				}

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
	}

	@Override
	public void setTaxList(List<Tax> data) {
		taxCellTable.setList(data);

		for (Tax tax : data) {
			taxSuggestions.add(tax.getName());
		}

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

	@UiHandler("selectTaxButton")
	protected void onCLickSelectTaxButton(ClickEvent event) {
		taxCellTable.clearSelected();
		taxModal.show();
	}

	@UiHandler("taxModalCancelButton")
	protected void onCLickTaxModalCancelButton(ClickEvent event) {
		taxModal.hide();
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
		validationPanel.setHTML(error);
	}

	/**
	 * Show form fields error
	 * 
	 * @param field
	 * @param error
	 */
	public void setFieldError(String field, String error) {
		if (field.equals("name"))
			nameCG.setType(ControlGroupType.ERROR);

		else if (field.equals("description"))
			descriptionCG.setType(ControlGroupType.ERROR);

		else if (field.equals("price"))
			priceCG.setType(ControlGroupType.ERROR);
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		nameCG.setType(ControlGroupType.NONE);
		descriptionCG.setType(ControlGroupType.NONE);
		priceCG.setType(ControlGroupType.NONE);

		validationButton.setVisible(false);
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

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}
