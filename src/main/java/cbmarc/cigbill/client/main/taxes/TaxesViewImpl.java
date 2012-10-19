package cbmarc.cigbill.client.main.taxes;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.ui.AppCellTable;
import cbmarc.cigbill.client.utils.IFilter;
import cbmarc.cigbill.shared.Tax;

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

public class TaxesViewImpl extends Composite implements TaxesView, Editor<Tax> {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, TaxesViewImpl> {
	}

	public interface Driver extends SimpleBeanEditorDriver<Tax, TaxesViewImpl> {
	}

	@UiField(provided = true)
	AppCellTable<Tax> cellTable = new AppCellTable<Tax>(new IFilter<Tax>() {
		@Override
		public boolean isValid(Tax value, String filter) {
			if (filter == null || value == null)
				return true;
			return value.getName().toLowerCase().contains(filter.toLowerCase())
					|| value.getDescription().contains(filter.toLowerCase());
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
	TextBox description;
	@UiField
	SubmitButton submitButton;
	@UiField
	Button backButton;
	@UiField
	Button formDeleteButton;

	// Control groups for mark errors
	@UiField
	DivElement nameCG;
	@UiField
	DivElement descriptionCG;

	private Presenter presenter;

	private AppConstants appConstants = GWT.create(AppConstants.class);
	private TaxesConstants taxesConstants = GWT.create(TaxesConstants.class);

	/**
	 * Constructor
	 */
	public TaxesViewImpl() {
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
		Column<Tax, SafeHtml> nameColumn = new Column<Tax, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Tax object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				Anchor anchor = new Anchor(object.getName());
				anchor.setHref("#taxes:edit/" + object.getId());

				sb.appendHtmlConstant(anchor.toString());

				return sb.toSafeHtml();
			}
		};
		cellTable.getCellTable().addColumn(nameColumn,
				taxesConstants.columnName());

		// Make column sortable.
		nameColumn.setSortable(true);
		cellTable.getListHandler().setComparator(nameColumn,
				new Comparator<Tax>() {
					public int compare(Tax o1, Tax o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		// /////////////////////////////////////////////////////////////////////
		// DESCRIPTION COLUMN
		TextColumn<Tax> descriptionColumn = new TextColumn<Tax>() {

			@Override
			public String getValue(Tax object) {
				return object.getDescription();
			}
		};
		cellTable.getCellTable().addColumn(descriptionColumn,
				taxesConstants.columnDescription());

		// Make column sortable.
		descriptionColumn.setSortable(true);
		cellTable.getListHandler().setComparator(descriptionColumn,
				new Comparator<Tax>() {
					public int compare(Tax o1, Tax o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
		// /////////////////////////////////////////////////////////////////////
		// CELLTABLE CLICKHANDLER
		cellTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<Tax> selectedSet = cellTable.getSelectionModel()
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
	public void setList(List<Tax> list) {
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
			Set<Tax> items = cellTable.getSelectionModel().getSelectedSet();
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
		presenter.goTo(new TaxesPlace("add"));
	}

	@UiHandler("backButton")
	protected void onCLickCancelButton(ClickEvent event) {
		presenter.goTo(new TaxesPlace());
	}

	@UiHandler("toolbarRefreshButton")
	protected void onCLickToolbarRefreshButton(ClickEvent event) {
		presenter.doLoad();
	}

	@UiHandler("formDeleteButton")
	protected void onClickFormDeleteButton(ClickEvent event) {
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

		validationAnchor.setVisible(false);
		validationPanel.setVisible(false);
	}

	/**
	 * Create a editor driver
	 */
	@Override
	public SimpleBeanEditorDriver<Tax, ?> createEditorDriver() {
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