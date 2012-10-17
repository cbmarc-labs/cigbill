package cbmarc.cigbill.client.main.customers;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.utils.IFilter;
import cbmarc.cigbill.shared.Customer;

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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

@Singleton
public class CustomersViewImpl extends Composite implements CustomersView,
		Editor<Customer> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, CustomersViewImpl> {
	}

	public interface Driver extends
			SimpleBeanEditorDriver<Customer, CustomersViewImpl> {
	}

	@UiField(provided = true)
	AppCellTable<Customer> cellTable = new AppCellTable<Customer>(
			new IFilter<Customer>() {
				@Override
				public boolean isValid(Customer value, String filter) {
					if (filter == null || value == null)
						return true;
					return value.getName().toLowerCase()
							.contains(filter.toLowerCase())
							|| value.getEmail().toLowerCase()
									.contains(filter.toLowerCase());
				}
			});

	@Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel cellTablePanel;
	@UiField
	Button addTableButton;
	@UiField
	Button deleteTableButton;

	// Validatior error messages
	@UiField
	Anchor validationAnchor;
	@UiField
	FocusPanel validationPanel;

	// Form fields
	@UiField
	FormPanel formPanel;
	@UiField
	TextBox name;
	@UiField
	TextBox email;
	@UiField
	SubmitButton submitButton;
	@UiField
	Button backButton;

	// Control groups for mark errors
	@UiField
	DivElement nameCG;
	@UiField
	DivElement emailCG;

	private Presenter presenter;

	/**
	 * Constructor
	 */
	public CustomersViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		// hide by default
		cellTablePanel.setVisible(false);
		//formPanel.setVisible(false);
		
		createCellTable();
	}

	/**
	 * Create celltable columns
	 */
	private void createCellTable() {
		// NAME COLUMN
		Column<Customer, SafeHtml> nameColumn = new Column<Customer, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Customer object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				Anchor anchor = new Anchor(object.getName());
				anchor.setHref("#main:customers/edit/" + object.getId());

				sb.appendHtmlConstant(anchor.toString());

				return sb.toSafeHtml();
			}
		};
		cellTable.getCellTable().addColumn(nameColumn, "Name");

		// Make the first name column sortable.
		nameColumn.setSortable(true);
		cellTable.getListHandler().setComparator(nameColumn,
				new Comparator<Customer>() {
					public int compare(Customer o1, Customer o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		TextColumn<Customer> emailColumn = new TextColumn<Customer>() {

			@Override
			public String getValue(Customer object) {
				return object.getEmail();
			}
		};
		cellTable.getCellTable().addColumn(emailColumn, "Email");
		emailColumn.setSortable(true);
		cellTable.getListHandler().setComparator(emailColumn,
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
				Set<Customer> selectedSet = cellTable.getSelectionModel()
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
		if (Window.confirm("Are you sure?")) {
			Set<Customer> items = cellTable.getSelectionModel()
					.getSelectedSet();
			presenter.doDelete(items);
		}
	}

	@UiHandler(value = { "validationPanel", "validationAnchor" })
	protected void onClickValidation(ClickEvent event) {
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
		//presenter.goTo(new MainPlace("customers/add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
		//presenter.goTo(new MainPlace("customers"));
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

		else if (field.equals("email"))
			divElement = emailCG;

		if (divElement != null)
			divElement.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		String styleName = "control-group";

		nameCG.setClassName(styleName);
		emailCG.setClassName(styleName);

		validationAnchor.setVisible(false);
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
