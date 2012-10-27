package cbmarc.cigbill.client.main;

import javax.inject.Singleton;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
import cbmarc.cigbill.client.main.home.HomePlace;
import cbmarc.cigbill.client.main.invoices.InvoicesPlace;
import cbmarc.cigbill.client.main.payments.PaymentsPlace;
import cbmarc.cigbill.client.main.products.ProductsPlace;
import cbmarc.cigbill.client.main.taxes.TaxesPlace;
import cbmarc.cigbill.client.main.users.UsersPlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@Singleton
public class TopActivity extends AbstractActivity implements
		PlaceChangeEvent.Handler, TopView.Presenter {

	@Inject
	PlaceController placeController;
	@Inject
	AppConstants appConstants;
	@Inject
	TopView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		view.setPresenter(this);

		updateAppBreadcrumbs();

		eventBus.addHandler(PlaceChangeEvent.TYPE, this);

	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		updateAppBreadcrumbs();

	}

	private void updateAppBreadcrumbs() {
		// return when not mainplace instance
		if (!(placeController.getWhere() instanceof MainPlace))
			return;

		MainPlace mainPlace = ((MainPlace) placeController.getWhere());

		String[] tokens = mainPlace.getSplitToken();

		view.getAppBreadcrumbs().clear();
		view.getAppBreadcrumbs().addCrumb(appConstants.breadcrumbHome(),
				new HomePlace());

		Place where = placeController.getWhere();
		if (!(where instanceof HomePlace)) {

			if (where instanceof InvoicesPlace) {
				view.getAppBreadcrumbs().addCrumb(appConstants.navInvoices(),
						new InvoicesPlace());

			} else if (where instanceof PaymentsPlace) {
				view.getAppBreadcrumbs().addCrumb(appConstants.navPayments(),
						new PaymentsPlace());

			} else if (where instanceof ProductsPlace) {
				view.getAppBreadcrumbs().addCrumb(appConstants.navProducts(),
						new ProductsPlace());

			} else if (where instanceof TaxesPlace) {
				view.getAppBreadcrumbs().addCrumb(appConstants.navTaxes(),
						new TaxesPlace());

			} else if (where instanceof CustomersPlace) {
				view.getAppBreadcrumbs().addCrumb(appConstants.navCustomers(),
						new CustomersPlace());

			} else if (where instanceof UsersPlace) {
				view.getAppBreadcrumbs().addCrumb(appConstants.navUsers(),
						new UsersPlace());

			} /*
			 * else if(where instanceof MessagesPlace) {
			 * view.getAppBreadcrumbs().addCrumb(name, new MessagesPlace());
			 * 
			 * }
			 */

			if (!tokens[0].isEmpty())
				view.getAppBreadcrumbs().addCrumb(tokens[0]);
		}
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

}
