package cbmarc.cigbill.client.main;

import javax.inject.Singleton;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@Singleton
public class BreadcrumbActivity extends AbstractActivity implements
		BreadcrumbView.Presenter, PlaceChangeEvent.Handler {

	@Inject
	PlaceController placeController;

	@Inject
	AppConstants appConstants;

	@Inject
	BreadcrumbView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		Window.alert("kkk");
		panel.setWidget(view);

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

		view.clear();
		view.addCrumb(appConstants.breadcrumbHome(), new InvoicesPlace());

		Place where = placeController.getWhere();

		String text = null;
		Place place = null;

		if (where instanceof InvoicesPlace) {
			text = appConstants.navInvoices();
			place = new InvoicesPlace();

		} else if (where instanceof PaymentsPlace) {
			text = appConstants.navPayments();
			place = new PaymentsPlace();

		} else if (where instanceof ProductsPlace) {
			text = appConstants.navProducts();
			place = new ProductsPlace();

		} else if (where instanceof TaxesPlace) {
			text = appConstants.navTaxes();
			place = new TaxesPlace();

		} else if (where instanceof CustomersPlace) {
			text = appConstants.navCustomers();
			place = new CustomersPlace();

		} else if (where instanceof UsersPlace) {
			text = appConstants.navUsers();
			place = new UsersPlace();

		}

		if (text != null) {
			if (tokens[0].isEmpty())
				place = null;

			view.addCrumb(text, place);
		}

		if (!tokens[0].isEmpty())
			view.addCrumb(tokens[0], null);

	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

}
