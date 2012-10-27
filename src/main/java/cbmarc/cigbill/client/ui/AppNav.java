package cbmarc.cigbill.client.ui;

import cbmarc.cigbill.client.auth.AuthPlace;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
import cbmarc.cigbill.client.main.invoices.InvoicesPlace;
import cbmarc.cigbill.client.main.payments.PaymentsPlace;
import cbmarc.cigbill.client.main.products.ProductsPlace;
import cbmarc.cigbill.client.main.taxes.TaxesPlace;
import cbmarc.cigbill.client.main.users.UsersPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppNav extends Composite {

	private static AppNavUiBinder uiBinder = GWT.create(AppNavUiBinder.class);

	interface AppNavUiBinder extends UiBinder<Widget, AppNav> {
	}

	private HandlerManager handlerManager = new HandlerManager(this);

	public AppNav() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler(value = { "money", "stock", "people", "settings" })
	protected void onClickValidation(ClickEvent event) {
		event.preventDefault();
		event.stopPropagation();
	}

	@UiHandler("invoices")
	void onClickInvoices(ClickEvent event) {
		changePlace(new InvoicesPlace());
	}

	@UiHandler("payments")
	void onClickPayments(ClickEvent event) {
		changePlace(new PaymentsPlace());
	}

	@UiHandler("products")
	void onClickProducts(ClickEvent event) {
		changePlace(new ProductsPlace());
	}

	@UiHandler("taxes")
	void onClickTaxes(ClickEvent event) {
		changePlace(new TaxesPlace());
	}

	@UiHandler("customers")
	void onClickCustomers(ClickEvent event) {
		changePlace(new CustomersPlace());
	}

	@UiHandler("users")
	void onClickUsers(ClickEvent event) {
		changePlace(new UsersPlace());
	}

	@UiHandler("messages")
	void onClickMessages(ClickEvent event) {
		//changePlace(new MessagesPlace());
	}
	
	@UiHandler("logout")
	void onClickLogout(ClickEvent event) {
		changePlace(new AuthPlace("logout"));
	}

	private void changePlace(Place place) {
		handlerManager.fireEvent(new PlaceChangeEvent(place));
	}

	public HandlerRegistration addPlaceChangeHandler(
			PlaceChangeEvent.Handler handler) {
		return handlerManager.addHandler(PlaceChangeEvent.TYPE, handler);
	}

}
