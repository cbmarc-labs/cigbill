package cbmarc.cigbill.client.ui;

import java.util.List;

import cbmarc.cigbill.client.utils.FilteredListDataProvider;
import cbmarc.cigbill.client.utils.IFilter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.DefaultSelectionEventManager.EventTranslator;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class AppCellTable<T> extends Composite implements HasClickHandlers {

	private static Binder binder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, AppCellTable<?>> {
	}

	public interface CellTableResource extends CellTable.Resources {
		public interface CellTableStyle extends CellTable.Style {
		};

		@Source({ "AppCellTable.css" })
		CellTableStyle cellTableStyle();
	}

	private CellTable.Resources tableResources = GWT
			.create(CellTableResource.class);
	private SimplePager.Resources pagerResources = GWT
			.create(SimplePager.Resources.class);

	@UiField
	ListBox lengthListBox;
	@UiField
	TextBox filterTextBox;
	@UiField(provided = true)
	CellTable<T> cellTable = new CellTable<T>(10, tableResources);
	@UiField(provided = true)
	SimplePager simplePager = new SimplePager(TextLocation.CENTER,
			pagerResources, false, 0, true);

	private FilteredListDataProvider<T> dataProvider;
	private ListHandler<T> listHandler;
	public MultiSelectionModel<T> selectionModel;

	private HandlerManager handlerManager = new HandlerManager(this);

	public AppCellTable(IFilter<T> filter) {
		dataProvider = new FilteredListDataProvider<T>(filter);
		dataProvider.addDataDisplay(cellTable);

		listHandler = new ListHandler<T>(dataProvider.getList());

		selectionModel = new MultiSelectionModel<T>();

		cellTable.setWidth("100%");
		cellTable.addColumnSortHandler(listHandler);
		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager
						.createCustomManager(new EventTranslator<T>() {

							@Override
							public boolean clearCurrentSelection(
									CellPreviewEvent<T> event) {
								// dont want to deselect all when one is
								// selected
								return false;
							}

							@Override
							public SelectAction translateSelectionEvent(
									CellPreviewEvent<T> event) {
								NativeEvent nativeEvent = event
										.getNativeEvent();
								if ("click".equals(nativeEvent.getType())) {

									// yeahhh, finally i got the magic click
									Element target = nativeEvent
											.getEventTarget().cast();
									if ("div".equals(target.getTagName()
											.toLowerCase())
											|| "td".equals(target.getTagName()
													.toLowerCase())) {

										return SelectAction.TOGGLE;
									}
								}

								return SelectAction.IGNORE;
							}
						}));

		simplePager.setDisplay(cellTable);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						handlerManager.fireEvent(new ClickEvent() {
						});

					}
				});

		initWidget(binder.createAndBindUi(this));
	}

	public CellTable<T> getCellTable() {
		return cellTable;
	}

	public FilteredListDataProvider<T> getDataProvider() {
		return dataProvider;
	}

	public MultiSelectionModel<T> getSelectionModel() {
		return selectionModel;
	}

	public ListHandler<T> getListHandler() {
		return listHandler;
	}

	public void setFocus() {
		filterTextBox.setFocus(true);
	}

	public void setList(List<T> list) {
		List<T> l = dataProvider.getList();

		selectionModel.clear();

		l.clear();
		l.addAll(list);
	}

	public void setStyleName(String style) {
		cellTable.setStyleName(style);
	}

	@UiHandler("lengthListBox")
	protected void onValueChange(ChangeEvent event) {
		int index = lengthListBox.getSelectedIndex();

		cellTable.setPageSize(Integer.parseInt(lengthListBox.getValue(index)));
	}

	@UiHandler("filterTextBox")
	protected void onValueChange(KeyUpEvent e) {
		dataProvider.setFilter(filterTextBox.getText());
		dataProvider.refresh();
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return handlerManager.addHandler(ClickEvent.getType(), handler);
	}
}
