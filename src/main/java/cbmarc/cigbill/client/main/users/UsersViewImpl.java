package cbmarc.cigbill.client.main.users;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.invoices.InvoicesPlace;
import cbmarc.cigbill.client.ui.AppCheckboxCellTable;
import cbmarc.cigbill.client.ui.ListBoxEditor;
import cbmarc.cigbill.client.ui.ListBoxMultiEditor;
import cbmarc.cigbill.shared.User;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

@Singleton
public class UsersViewImpl extends Composite implements UsersView, Editor<User> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, UsersViewImpl> {
	}

	public interface Driver extends SimpleBeanEditorDriver<User, UsersViewImpl> {
	}

	@UiField
	AppCheckboxCellTable<User> cellTable;

	@Ignore
	@UiField
	Label legend;

	@UiField
	HTMLPanel cellTablePanel;

	@UiField
	Button addTableButton, deleteTableButton, toolbarRefreshButton;

	@UiField
	Button validationButton;

	@UiField
	HTMLPanel validationPanel;

	// Form fields
	@UiField
	FormPanel formPanel;

	@UiField
	TextBox login;

	@UiField
	PasswordTextBox password;

	@UiField
	ListBoxEditor sex;

	@UiField
	ListBoxMultiEditor favoriteColor;

	@UiField
	TextArea description;

	@UiField
	CheckBox active;

	@UiField
	SubmitButton submitButton;

	@UiField
	Button backButton, formDeleteButton;

	// Control groups for mark errors
	@UiField
	DivElement loginCG, passwordCG, sexCG, descriptionCG;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;
	
	private UsersConstants usersConstants = GWT.create(UsersConstants.class);

	/**
	 * Constructor
	 */
	public UsersViewImpl() {
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
		// LOGIN COLUMN
		TextColumn<User> loginColumn = new TextColumn<User>(){

			@Override
			public String getValue(User object) {
				return object.getLogin();
			}};
		cellTable.addColumn(loginColumn, usersConstants.columnLogin());

		// Make the first name column sortable.
		loginColumn.setSortable(true);
		cellTable.setComparator(loginColumn, new Comparator<User>() {
			public int compare(User o1, User o2) {
				return o1.getLogin().compareTo(o2.getLogin());
			}
		});

		// /////////////////////////////////////////////////////////////////////
		// CREATED COLUMN
		DateCell createdDateCell = new DateCell(
				DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss"));
		Column<User, Date> createdColumn = new Column<User, Date>(
				createdDateCell) {

			@Override
			public Date getValue(User object) {
				return object.getCreated();
			}
		};
		cellTable.addColumn(createdColumn, usersConstants.columnCreated());

		// /////////////////////////////////////////////////////////////////////
		// LAST UPDATED COLUMN
		DateCell lastUpdatedDateCell = new DateCell(
				DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss"));
		Column<User, Date> lastUpdatedColumn = new Column<User, Date>(
				lastUpdatedDateCell) {

			@Override
			public Date getValue(User object) {
				return object.getUpdated();
			}
		};
		cellTable.addColumn(lastUpdatedColumn, usersConstants.columnUpdated());

		// /////////////////////////////////////////////////////////////////////
		// ACTIVE COLUMN
		Column<User, Boolean> activeColumn = new Column<User, Boolean>(
				new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(User object) {
				return object.getActive();
			}
		};
		activeColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		activeColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		activeColumn.setFieldUpdater(new FieldUpdater<User, Boolean>() {

			@Override
			public void update(int index, User object, Boolean value) {
				object.setActive(value);

			}
		});
		cellTable.addColumn(activeColumn, usersConstants.columnActive());
		cellTable.setColumnWidth(activeColumn, "1em");

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<User> selectedSet = cellTable.getSelectedSet();

				boolean visible = selectedSet.size() > 0 ? true : false;
				deleteTableButton.setVisible(visible);
				
				if (cellTable.getSelected() != null) {
					String id = cellTable.getSelected().getId().toString();
					presenter.goTo(new UsersPlace("edit/" + id));
				}
			}
		});

	}

	/**
	 * Set list data to cellTable
	 * 
	 * @param list
	 */
	public void setList(List<User> list) {
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
		presenter.goTo(new UsersPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
		presenter.goTo(new UsersPlace());
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

		if (field.equals("login"))
			loginCG.setClassName("control-group error");

		else if (field.equals("password"))
			passwordCG.setClassName("control-group error");

		else if (field.equals("sex"))
			sexCG.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		loginCG.setClassName("control-group");
		passwordCG.setClassName("control-group");
		sexCG.setClassName("control-group");

		validationButton.setVisible(false);
		validationPanel.setVisible(false);
	}

	/**
	 * Create a editor driver
	 */
	@Override
	public SimpleBeanEditorDriver<User, ?> createEditorDriver() {
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
				login.setFocus(true);

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
