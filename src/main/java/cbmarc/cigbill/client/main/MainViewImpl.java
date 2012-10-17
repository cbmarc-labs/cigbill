package cbmarc.cigbill.client.main;

import java.util.HashMap;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.ui.LiPanel;
import cbmarc.cigbill.client.ui.UlPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MainViewImpl extends Composite implements MainView {

	private static MainViewImplUiBinder uiBinder = GWT
			.create(MainViewImplUiBinder.class);

	interface MainViewImplUiBinder extends UiBinder<Widget, MainViewImpl> {
	}

	private AppConstants appConstants = GWT.create(AppConstants.class);
	private HashMap<String, String> BreadCrumbCnts = new HashMap<String, String>();

	private static String urlBreadcrumb = "#main:";
	private static String urlBreadcrumbHome = urlBreadcrumb + "invoices";

	@UiField
	UlPanel breadcrumb;
	@UiField
	SimplePanel contentPanel;

	@Inject
	public MainViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		/*ActivityManager mainActivityManager = new ActivityManager(
				mainActivityMapper, mainEventBus);
		mainActivityManager.setDisplay(contentPanel);*/

		/*
		 * ActivityMapper mainActivityMapper = new MainActivityMapper();
		 * ActivityManager mainActivityManager = new ActivityManager(
		 * mainActivityMapper, eventBus);
		 * mainActivityManager.setDisplay(contentPanel);
		 */

		// constants = clientFactory.getConstants();

		initialize();
	}

	private void initialize() {
		BreadCrumbCnts.put("money", appConstants.navMoney());
		BreadCrumbCnts.put("invoices", appConstants.navInvoices());
		BreadCrumbCnts.put("payments", appConstants.navPayments());
		BreadCrumbCnts.put("stock", appConstants.navStock());
		BreadCrumbCnts.put("products", appConstants.navProducts());
		BreadCrumbCnts.put("taxes", appConstants.navTaxes());
		BreadCrumbCnts.put("people", appConstants.navPeople());
		BreadCrumbCnts.put("customers", appConstants.navCustomers());
		BreadCrumbCnts.put("users", appConstants.navUsers());
		BreadCrumbCnts.put("settings", appConstants.navSettings());
		BreadCrumbCnts.put("messages", appConstants.navMessages());

		BreadCrumbCnts.put("add", appConstants.breadcrumbAdd());
		BreadCrumbCnts.put("edit", appConstants.breadcrumbEdit());
	}

	@Override
	public void setBreadcrumb(String[] splitToken) {
		breadcrumb.clear();

		setBreadcrumbSection(appConstants.breadcrumbHome(), urlBreadcrumbHome,
				false, splitToken[0] == null ? false : true);

		// #main:users[0]/add[1]
		if (splitToken[0] != null) {

			String section = BreadCrumbCnts.get(splitToken[0]);
			if (section == null)
				section = appConstants.breadcrumbUndefined();

			setBreadcrumbSection(section, urlBreadcrumb + splitToken[0],
					splitToken[1] == null ? true : false,
					splitToken[1] == null ? false : true);
		}

		if (splitToken[1] != null) {
			String action = BreadCrumbCnts.get(splitToken[1]);
			if (action == null)
				action = appConstants.breadcrumbUndefined();

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

	@Override
	public SimplePanel getContentPanel() {
		return contentPanel;
	}

}
