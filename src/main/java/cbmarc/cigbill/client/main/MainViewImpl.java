package cbmarc.cigbill.client.main;

import java.util.HashMap;

import cbmarc.cigbill.client.ClientFactory;
import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.mvp.MainActivityMapper;
import cbmarc.cigbill.client.ui.LiPanel;
import cbmarc.cigbill.client.ui.UlPanel;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MainViewImpl extends Composite implements MainView {

	private static MainViewImplUiBinder uiBinder = GWT
			.create(MainViewImplUiBinder.class);

	interface MainViewImplUiBinder extends UiBinder<Widget, MainViewImpl> {
	}

	private AppConstants constants;
	private HashMap<String, String> BreadCrumbCnts = new HashMap<String, String>();
	
	private static String urlBreadcrumb = "#main:";
	private static String urlBreadcrumbHome = urlBreadcrumb + "invoices";

	@UiField
	UlPanel breadcrumb;
	@UiField
	SimplePanel contentPanel;

	public MainViewImpl(ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));

		ActivityMapper mainActivityMapper = new MainActivityMapper(
				clientFactory);
		ActivityManager mainActivityManager = new ActivityManager(
				mainActivityMapper, clientFactory.getEventBus());
		mainActivityManager.setDisplay(contentPanel);

		constants = clientFactory.getConstants();

		initialize();
	}

	private void initialize() {
		BreadCrumbCnts.put("money", constants.navMoney());
		BreadCrumbCnts.put("invoices", constants.navInvoices());
		BreadCrumbCnts.put("payments", constants.navPayments());
		BreadCrumbCnts.put("stock", constants.navStock());
		BreadCrumbCnts.put("products", constants.navProducts());
		BreadCrumbCnts.put("taxes", constants.navTaxes());
		BreadCrumbCnts.put("people", constants.navPeople());
		BreadCrumbCnts.put("customers", constants.navCustomers());
		BreadCrumbCnts.put("users", constants.navUsers());
		BreadCrumbCnts.put("settings", constants.navSettings());
		BreadCrumbCnts.put("messages", constants.navMessages());
		
		BreadCrumbCnts.put("add", constants.breadcrumbAdd());
		BreadCrumbCnts.put("edit", constants.breadcrumbEdit());
	}

	@Override
	public void setBreadcrumb(String[] splitToken) {
		breadcrumb.clear();

		setBreadcrumbSection(constants.breadcrumbHome(), urlBreadcrumbHome,
				false, splitToken[0] == null ? false : true);

		// #main:users[0]/add[1]
		if (splitToken[0] != null) {

			String section = BreadCrumbCnts.get(splitToken[0]);
			if (section == null)
				section = constants.breadcrumbUndefined();

			setBreadcrumbSection(section, urlBreadcrumb + splitToken[0],
					splitToken[1] == null ? true : false,
					splitToken[1] == null ? false : true);
		}

		if (splitToken[1] != null) {
			String action = BreadCrumbCnts.get(splitToken[1]);
			if (action == null)
				action = constants.breadcrumbUndefined();
			
			setBreadcrumbSection(action, null, true, false);
		}
	}

	/**
	 * Set breadcrumb section
	 * 
	 * @param text
	 * @param href
	 * @param active
	 * @param divider
	 */
	private void setBreadcrumbSection(String text, String href, boolean active,
			boolean divider) {
		LiPanel liPanel = new LiPanel();

		if (active) {
			liPanel.setStyleName("active");
			liPanel.setText(text);

		} else {
			Anchor anchor = new Anchor(text);
			anchor.setHref(href);

			liPanel.add(anchor);

			if (divider) {
				SpanElement spanElement = Document.get().createSpanElement();
				spanElement.setClassName("divider");
				spanElement.setInnerText("/");

				liPanel.add(spanElement);
			}

		}

		breadcrumb.add(liPanel);
	}

}
