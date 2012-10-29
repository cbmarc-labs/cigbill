package cbmarc.cigbill.client.main.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;
import com.google.inject.Singleton;

@Singleton
public class HomeViewImpl extends Composite implements HomeView {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, HomeViewImpl> {
	}

	@UiField
	HasOneWidget pieChartContainer;

	private PieChart pieChart;

	/**
	 * Constructor
	 */
	public HomeViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		loadVisualizationApi();
	}

	public void loadVisualizationApi() {

		Runnable onLoadCallback = new Runnable() {

			@Override
			public void run() {
				drawChart();

			}
		};

		VisualizationUtils.loadVisualizationApi(onLoadCallback,
				PieChart.PACKAGE);
	}

	public void drawChart() {
		pieChart = new PieChart(createTable(), createOptions());

		pieChartContainer.setWidget(pieChart);

	}

	private AbstractDataTable createTable() {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Hours per Day");
		data.addRows(2);
		data.setValue(0, 0, "Work");
		data.setValue(0, 1, 14);
		data.setValue(1, 0, "Sleep");
		data.setValue(1, 1, 10);
		return data;
	}

	private Options createOptions() {
		Options options = Options.create();

		options.setWidth(400);
		options.setHeight(240);
		options.set3D(true);
		options.setTitle("My Daily Activities");

		return options;
	}

}
