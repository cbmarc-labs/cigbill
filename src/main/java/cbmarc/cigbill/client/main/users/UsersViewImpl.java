package cbmarc.cigbill.client.main.users;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.ui.ListBoxEditor;
import cbmarc.cigbill.client.ui.ListBoxMultiEditor;
import cbmarc.cigbill.client.utils.IFilter;
import cbmarc.cigbill.shared.User;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
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

	@UiField(provided = true)
	AppCellTable<User> cellTable = new AppCellTable<User>(new IFilter<User>() {
		@Override
		public boolean isValid(User value, String filter) {
			if (filter == null || value == null)
				return true;
			return value.getLogin().toLowerCase()
					.contains(filter.toLowerCase());// ||
			// Integer.toString(value.getName()).contains(filter.toLowerCase());
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
	TextBox login;
	@UiField
	PasswordTextBox password;
	@Ignore
	@UiField
	PasswordTextBox confirmPassword;
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
	DivElement loginCG, passwordCG, confirmPasswordCG, sexCG, descriptionCG;

	private Presenter presenter;

	@Inject
	private AppConstants appConstants;
	private UsersConstants usersConstants = GWT.create(UsersConstants.class);
	
	Column<User, SafeHtml> loginColumn;

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
		loginColumn = new Column<User, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(User object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				Anchor anchor = new Anchor(object.getLogin());
				anchor.setHref("#users:edit/" + object.getId());

				sb.appendHtmlConstant(anchor.toString());

				return sb.toSafeHtml();
			}
		};
		cellTable.getCellTable().addColumn(loginColumn,
				usersConstants.columnLogin());

		// Make the first name column sortable.
		loginColumn.setSortable(true);
		cellTable.getListHandler().setComparator(loginColumn,
				new Comparator<User>() {
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
		cellTable.getCellTable().addColumn(createdColumn,
				usersConstants.columnCreated());

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
		cellTable.getCellTable().addColumn(lastUpdatedColumn,
				usersConstants.columnUpdated());

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
		cellTable.getCellTable().addColumn(activeColumn,
				usersConstants.columnActive());
		cellTable.getCellTable().setColumnWidth(activeColumn, 1.0, Unit.EM);

		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<User> selectedSet = cellTable.getSelectionModel()
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
	public void setList(List<User> list) {
		cellTable.setList(list);
		cellTable.getCellTable().getColumnSortList().clear();
		cellTable.getCellTable().getColumnSortList().push(loginColumn);
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
			Set<User> users = cellTable.getSelectionModel().getSelectedSet();
			presenter.doDelete(users);
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

		if (field.equals("login"))
			divElement = loginCG;

		else if (field.equals("password"))
			divElement = passwordCG;

		else if (field.equals("confirmPassword"))
			divElement = confirmPasswordCG;

		else if (field.equals("sex"))
			divElement = sexCG;

		if (divElement != null)
			divElement.setClassName("control-group error");
	}

	/**
	 * Clear errors from form
	 */
	public void clearErrors() {
		String styleName = "control-group";

		loginCG.setClassName(styleName);
		passwordCG.setClassName(styleName);
		confirmPasswordCG.setClassName(styleName);
		sexCG.setClassName(styleName);

		validationAnchor.setVisible(false);
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

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				cellTable.setFocus();

			}
		});

	}

	@Override
	public String getConfirmPassword() {
		return confirmPassword.getValue();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}
