package cbmarc.cigbill.client.ui;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.DefaultSelectionEventManager.EventTranslator;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;

public class AppCellTable<T> extends Composite implements HasClickHandlers,
		SelectionChangeEvent.Handler, EventTranslator<T> {

	private static Binder binder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, AppCellTable<?>> {
	}

	interface CellTableResource extends CellTable.Resources {

		interface CellTableStyle extends CellTable.Style {
		};

		@Source({ "AppCellTable.css" })
		CellTableStyle cellTableStyle();
	}

	private CellTable.Resources tableResources = GWT
			.create(CellTableResource.class);

	@UiField
	Panel pagination;

	@UiField
	Anchor fastBackward, backward, count, forward, fastForward;

	@UiField(provided = true)
	CellTable<T> cellTable = new CellTable<T>(10, tableResources);

	private ListDataProvider<T> listDataProvider = new ListDataProvider<T>();

	private MultiSelectionModel<T> selectionModel = new MultiSelectionModel<T>();

	private SimplePager simplePager = new SimplePager();

	private ListHandler<T> listHandler = new ListHandler<T>(
			listDataProvider.getList());

	private HandlerManager handlerManager = new HandlerManager(this);

	public AppCellTable() {
		cellTable.setStyleName("table table-condensed");
		cellTable.addColumnSortHandler(listHandler);
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.createCustomManager(this));
		
		listDataProvider.addDataDisplay(cellTable);
		simplePager.setDisplay(cellTable);
		selectionModel.addSelectionChangeHandler(this);

		initWidget(binder.createAndBindUi(this));
	}

	public Set<T> getSelectedSet() {
		return selectionModel.getSelectedSet();
	}

	public T getSelected() {
		if (selectionModel.getSelectedSet().isEmpty())
			return null;

		return selectionModel.getSelectedSet().iterator().next();
	}

	public void clearSelected() {
		selectionModel.clear();
	}

	@SuppressWarnings("serial")
	public void addColumn(final Column<T, ?> col, final String headerString) {
		// set header horizontal aligment
		if (col.getHorizontalAlignment() != null) {
			cellTable.addColumn(col, new SafeHtml() {

				@Override
				public String asString() {
					return "<div style=\"text-align:"
							+ col.getHorizontalAlignment().getTextAlignString()
							+ ";\">" + headerString + "</div>";
				}
			});

		} else {
			cellTable.addColumn(col, headerString);

		}
	}

	public void setColumnWidth(Column<T, ?> column, String width) {
		cellTable.setColumnWidth(column, width);
	}

	public void setColumnWidth(Column<T, ?> column, double width, Unit unit) {
		cellTable.setColumnWidth(column, width, unit);
	}

	public void setComparator(Column<T, ?> column, Comparator<T> comparator) {
		listHandler.setComparator(column, comparator);
	}

	public void setPagination(Boolean visible) {
		pagination.setVisible(visible);
	}

	public void setList(List<T> list) {
		listDataProvider.getList().clear();
		listDataProvider.getList().addAll(list);
		listDataProvider.flush();

		selectionModel.clear();

		cellTable.getColumnSortList().clear();
		cellTable.getColumnSortList().push(cellTable.getColumn(0));
		cellTable.redraw();

		simplePager.firstPage();

		updatePagination();
	}

	public ListDataProvider<T> getListDataProvider() {
		return listDataProvider;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return handlerManager.addHandler(ClickEvent.getType(), handler);
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent event) {
		handlerManager.fireEvent(new ClickEvent() {
		});

	}

	@UiHandler("fastBackward")
	protected void onClickFastBackward(ClickEvent event) {
		simplePager.firstPage();
		updatePagination();
	}

	@UiHandler("backward")
	protected void onClickBackward(ClickEvent event) {
		simplePager.previousPage();
		updatePagination();
	}

	@UiHandler("forward")
	protected void onClickForward(ClickEvent event) {
		simplePager.nextPage();
		updatePagination();
	}

	@UiHandler("fastForward")
	protected void onClickFastForward(ClickEvent event) {
		simplePager.lastPage();
		updatePagination();
	}

	protected void updatePagination() {
		fastBackward.setEnabled(!simplePager.hasPreviousPage());
		backward.setEnabled(!simplePager.hasPreviousPage());

		HasRows display = simplePager.getDisplay();
		Range range = display.getVisibleRange();
		int start = range.getStart();

		int end = start + range.getLength();
		if (end > display.getRowCount())
			end = display.getRowCount();

		count.setText(start + " - " + end + " : " + display.getRowCount());

		forward.setEnabled(!simplePager.hasNextPage());
		fastForward.setEnabled(!simplePager.hasNextPage());
	}

	@Override
	public boolean clearCurrentSelection(CellPreviewEvent<T> event) {
		// dont want to deselect all when one is selected
		return false;
	}

	@Override
	public SelectAction translateSelectionEvent(CellPreviewEvent<T> event) {
		NativeEvent nativeEvent = event.getNativeEvent();

		if ("click".equals(nativeEvent.getType())) {

			// yeahhh, finally i got the magic click
			Element target = nativeEvent.getEventTarget().cast();

			if ("div".equals(target.getTagName().toLowerCase())
					|| "td".equals(target.getTagName().toLowerCase())) {

				return SelectAction.TOGGLE;

			}
		}

		return SelectAction.IGNORE;
	}

	/*
	 * NavLink prev = new NavLink("<", "javascript:;"); prev.addClickHandler(new
	 * ClickHandler() {
	 * 
	 * @Override public void onClick(ClickEvent event) { pager.previousPage();
	 * 
	 * }}); prev.setDisabled(!pager.hasPreviousPage()); taxPagination.add(prev);
	 * 
	 * int before = 2; while (!pager.hasPreviousPages(before) && before > 0) {
	 * before--; } int after = 2; while (!pager.hasNextPages(after) && after >
	 * 0) { after--; } for (int i = pager.getPage() - before; i <=
	 * pager.getPage() + after; i++) { final int index = i + 1; NavLink page =
	 * new NavLink(String.valueOf(index), "javascript:;");
	 * page.addClickHandler(new ClickHandler() {
	 * 
	 * @Override public void onClick(ClickEvent event) { pager.setPage(index);
	 * 
	 * }}); if (i == pager.getPage()) { page.setActive(true); }
	 * taxPagination.add(page); }
	 * 
	 * NavLink next = new NavLink(">", "javascript:;"); next.addClickHandler(new
	 * ClickHandler() {
	 * 
	 * @Override public void onClick(ClickEvent event) { pager.nextPage();
	 * 
	 * }}); next.setDisabled(!pager.hasNextPage()); taxPagination.add(next);
	 */
}
