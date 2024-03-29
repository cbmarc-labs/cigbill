package cbmarc.cigbill.client.main.payments;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.invoices.InvoicesPlace;
import cbmarc.cigbill.client.ui.AppCheckboxCellTable;
import cbmarc.cigbill.shared.Payment;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PaymentsViewImpl extends Composite implements PaymentsView,
		Editor<Payment> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, PaymentsViewImpl> {
	}

	public interface Driver extends
			SimpleBeanEditorDriver<Payment, PaymentsViewImpl> {
	}

	@UiField
	AppCheckboxCellTable<Payment> cellTable;

	@Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel cellTablePanel;

	@UiField
	Button addTableButton;

	@UiField
	Button deleteTableButton, toolbarRefreshButton;

	// Validatior error messages
	@UiField
	Button validationButton;

	@UiField
	HTMLPanel validationPanel;

	// Form fields
	@UiField
	FormPanel formPanel;

	@UiField
	TextBox name;

	@UiField
	SubmitButton submitButton;

	@UiField
	Button backButton, formDeleteButton;

	// Control groups for mark errors
	@UiField
	DivElement nameCG;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;

	private PaymentsConstants taxesConstants = GWT
			.create(PaymentsConstants.class);

	/**
	 * Constructor
	 */
	public PaymentsViewImpl() {
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
		TextColumn<Payment> nameColumn = new TextColumn<Payment>(){

			@Override
			public String getValue(Payment object) {
				return object.getName();
			}};
		cellTable.addColumn(nameColumn, taxesConstants.columnName());

		// Make column sortable.
		nameColumn.setSortable(true);
		cellTable.setComparator(nameColumn, new Comparator<Payment>() {
			public int compare(Payment o1, Payment o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Payment> selectedSet = cellTable.getSelectedSet();

				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteTableButton.setVisible(visible);
				
				if (cellTable.getSelected() != null) {
					String id = cellTable.getSelected().getId().toString();
					presenter.goTo(new PaymentsPlace("edit/" + id));
				}
			}
		});

	}

	/**
	 * Set list data to cellTable
	 * 
	 * @param list
	 */
	public void setList(List<Payment> list) {
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
		presenter.goTo(new PaymentsPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
		presenter.goTo(new PaymentsPlace());
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
		if (field.equals("name"))
			nameCG.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		nameCG.setClassName("control-group");

		validationButton.setVisible(false);
		validationPanel.setVisible(false);
	}

	/**
	 * Create a editor driver
	 */
	@Override
	public SimpleBeanEditorDriver<Payment, ?> createEditorDriver() {
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
