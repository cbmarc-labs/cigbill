package cbmarc.cigbill.client.main.customers;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.utils.IFilter;
import cbmarc.cigbill.shared.Customer;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

@Singleton
public class CustomersViewImpl extends Composite implements CustomersView,
		Editor<Customer> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, CustomersViewImpl> {
	}

	public interface Driver extends
			SimpleBeanEditorDriver<Customer, CustomersViewImpl> {
	}

	@UiField
	AppCellTable<Customer> cellTable;

	@Ignore
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
	TextBox name;
	@UiField
	TextBox email;
	@UiField
	SubmitButton submitButton;
	@UiField
	Button backButton, formDeleteButton;

	// Control groups for mark errors
	@UiField
	ControlGroup nameCG, emailCG;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;
	private CustomersConstants customersConstants = GWT
			.create(CustomersConstants.class);

	Column<Customer, SafeHtml> nameColumn;

	/**
	 * Constructor
	 */
	public CustomersViewImpl() {
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
		// NAME COLUMN
		nameColumn = new Column<Customer, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Customer object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				Anchor anchor = new Anchor(object.getName());
				anchor.setHref("#customers:edit/" + object.getId());

				sb.appendHtmlConstant(anchor.toString());

				return sb.toSafeHtml();
			}
		};
		cellTable.addColumn(nameColumn,
				customersConstants.columnName());

		// Make the first name column sortable.
		nameColumn.setSortable(true);
		cellTable.setComparator(nameColumn,
				new Comparator<Customer>() {
					public int compare(Customer o1, Customer o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		// EMAIL COLUMN
		TextColumn<Customer> emailColumn = new TextColumn<Customer>() {

			@Override
			public String getValue(Customer object) {
				return object.getEmail();
			}
		};
		cellTable.addColumn(emailColumn,
				customersConstants.columnEmail());
		emailColumn.setSortable(true);
		cellTable.setComparator(emailColumn,
				new Comparator<Customer>() {

					@Override
					public int compare(Customer o1, Customer o2) {
						return o1.getEmail().compareTo(o2.getEmail());
					}
				});

		// CELLTABLE CLICK EVENT
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Customer> selectedSet = cellTable.getSelectedSet();
				
				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteTableButton.setVisible(visible);
			}
		});

	}

	/**
	 * Set list data to cellTable
	 * 
	 * @param list
	 */
	public void setList(List<Customer> list) {
		cellTable.setList(list);
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
		presenter.goTo(new CustomersPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
		presenter.goTo(new CustomersPlace());
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
		if (field.equals("name"))
			nameCG.setType(ControlGroupType.ERROR);

		else if (field.equals("email"))
			emailCG.setType(ControlGroupType.ERROR);
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		nameCG.setType(ControlGroupType.NONE);
		emailCG.setType(ControlGroupType.NONE);

		validationButton.setVisible(false);
		validationPanel.setVisible(false);
	}

	/**
	 * Create a editor driver
	 */
	@Override
	public SimpleBeanEditorDriver<Customer, ?> createEditorDriver() {
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
