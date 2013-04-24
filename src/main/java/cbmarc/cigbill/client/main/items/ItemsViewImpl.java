package cbmarc.cigbill.client.main.items;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.taxes.TaxesConstants;
import cbmarc.cigbill.client.ui.AppCheckboxCellTable;
import cbmarc.cigbill.client.ui.AppTab;
import cbmarc.cigbill.client.utils.JavaScriptUtils;
import cbmarc.cigbill.shared.Item;

import com.google.gwt.cell.client.NumberCell;
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
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
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
import com.google.inject.Singleton;

@Singleton
public class ItemsViewImpl extends Composite implements ItemsView, Editor<Item> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ItemsViewImpl> {
	}

	public interface Driver extends SimpleBeanEditorDriver<Item, ItemsViewImpl> {
	}

	@UiField
	AppCheckboxCellTable<Item> itemsTable;

	@Editor.Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel tablePanel;

	@UiField
	Button addTableButton, deleteTableButton, toolbarRefreshButton;

	// Validatior error messages
	@UiField
	Button validationButton;

	@UiField
	HTMLPanel validationPanel;

	// Form fields
	@UiField
	FormPanel formPanel;
	
	@UiField
	AppTab generalTab;

	@UiField
	TextBox description;

	@UiField
	DoubleBox quantity, price;

	@UiField
	TextArea notes;

	@UiField
	SubmitButton submitButton;

	@UiField
	Button backButton, formDeleteButton;

	// Control groups for mark errors
	@UiField
	DivElement descriptionCG, priceCG, notesCG;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;

	private ItemsConstants itemsConstants = GWT.create(ItemsConstants.class);

	/**
	 * Constructor
	 */
	public ItemsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		// hide by default
		tablePanel.setVisible(false);
		formPanel.setVisible(false);

		createItemsTable();
	}

	/**
	 * Create celltable columns
	 */
	private void createItemsTable() {
		// /////////////////////////////////////////////////////////////////////
		// DESCRIPTION COLUMN
		TextColumn<Item> descriptionColumn = new TextColumn<Item>() {

			@Override
			public String getValue(Item object) {
				return object.getDescription();
			}
		};
		itemsTable.addColumn(descriptionColumn,
				itemsConstants.columnDescription());

		// /////////////////////////////////////////////////////////////////////
		// QUANTITY COLUMN
		Column<Item, Number> quantityColumn = new Column<Item, Number>(
				new NumberCell(NumberFormat.getFormat("#,##0.00"))) {
			@Override
			public Number getValue(Item object) {
				return object.getQuantity();
			}
		};
		quantityColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		itemsTable.addColumn(quantityColumn, itemsConstants.columnQuantity());
		itemsTable.setColumnWidth(quantityColumn, "8em");

		// /////////////////////////////////////////////////////////////////////
		// PRICE COLUMN
		Column<Item, Number> priceColumn = new Column<Item, Number>(
				new NumberCell(NumberFormat.getFormat("#,##0.00"))) {
			@Override
			public Number getValue(Item object) {
				return object.getPrice();
			}
		};
		priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		itemsTable.addColumn(priceColumn, itemsConstants.columnPrice());
		itemsTable.setColumnWidth(priceColumn, "8em");

		// Make the first name column sortable.
		priceColumn.setSortable(true);

		itemsTable.setComparator(priceColumn, new Comparator<Item>() {
			public int compare(Item o1, Item o2) {
				return o1.getPrice().compareTo(o2.getPrice());
			}
		});

		// /////////////////////////////////////////////////////////////////////
		// TOTAL COLUMN
		Column<Item, Number> totalColumn = new Column<Item, Number>(
				new NumberCell(NumberFormat.getFormat("#,##0.00"))) {
			@Override
			public Number getValue(Item object) {
				return object.getQuantity() * object.getPrice();
			}
		};
		totalColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		itemsTable.addColumn(totalColumn, itemsConstants.columnTotal());
		itemsTable.setColumnWidth(totalColumn, "8em");

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		itemsTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Item> selectedSet = itemsTable.getSelectedSet();

				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteTableButton.setVisible(visible);

				if (itemsTable.getSelected() != null) {
					String id = itemsTable.getSelected().getId().toString();
					presenter.goTo(new ItemsPlace("edit/" + id));
				}
			}
		});

	}

	/**
	 * Set list data to cellTable
	 * 
	 * @param list
	 */
	public void setList(List<Item> list) {
		itemsTable.setList(list);
	}

	@UiHandler("deleteTableButton")
	protected void onClickDeleteTableButton(ClickEvent event) {
		if (Window.confirm(appConstants.areYouSure())) {
			presenter.doDelete(itemsTable.getSelectedSet());
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
		presenter.goTo(new ItemsPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
		presenter.goTo(new ItemsPlace());
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
		if (field.equals("description"))
			descriptionCG.setClassName("control-group error");

		else if (field.equals("price"))
			priceCG.setClassName("control-group error");
		
		else if (field.equals("notes"))
			notesCG.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		descriptionCG.setClassName("control-group");
		priceCG.setClassName("control-group");
		notesCG.setClassName("control-group");

		validationButton.setVisible(false);
		validationPanel.setVisible(false);
	}

	/**
	 * Create a editor driver
	 */
	@Override
	public SimpleBeanEditorDriver<Item, ?> createEditorDriver() {
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
		
		generalTab.show();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				description.setFocus(true);

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
