package cbmarc.cigbill.client.main;

import cbmarc.cigbill.client.auth.AuthPlace;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
import cbmarc.cigbill.client.main.invoices.InvoicesPlace;
import cbmarc.cigbill.client.main.items.ItemsPlace;
import cbmarc.cigbill.client.main.payments.PaymentsPlace;
import cbmarc.cigbill.client.main.products.ProductsPlace;
import cbmarc.cigbill.client.main.taxes.TaxesPlace;
import cbmarc.cigbill.client.main.users.UsersPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class NavViewImpl extends Composite implements NavView {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, NavViewImpl> {
	}

	@UiField
	LIElement invoicesGroup, paymentsGroup, productsGroup, itemsGroup,
			taxesGroup, customersGroup, usersGroup;

	LIElement oldGroup = null;

	private Presenter presenter;

	public NavViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("invoices")
	void onClickInvoices(ClickEvent event) {
		presenter.goTo(new InvoicesPlace());
	}

	@UiHandler("payments")
	void onClickPayments(ClickEvent event) {
		presenter.goTo(new PaymentsPlace());
	}

	@UiHandler("products")
	void onClickProducts(ClickEvent event) {
		presenter.goTo(new ProductsPlace());
	}

	@UiHandler("items")
	void onClickItems(ClickEvent event) {
		presenter.goTo(new ItemsPlace());
	}

	@UiHandler("taxes")
	void onClickTaxes(ClickEvent event) {
		presenter.goTo(new TaxesPlace());
	}

	@UiHandler("customers")
	void onClickCustomers(ClickEvent event) {
		presenter.goTo(new CustomersPlace());
	}

	@UiHandler("users")
	void onClickUsers(ClickEvent event) {
		presenter.goTo(new UsersPlace());
	}

	@UiHandler("logout")
	void onClickLogout(ClickEvent event) {
		presenter.goTo(new AuthPlace("logout"));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	public void setActive(Place place) {
		LIElement liElement = null;

		if (place instanceof InvoicesPlace) {
			liElement = invoicesGroup;

		} else if (place instanceof PaymentsPlace) {
			liElement = paymentsGroup;

		} else if (place instanceof ProductsPlace) {
			liElement = productsGroup;

		} else if (place instanceof ItemsPlace) {
			liElement = itemsGroup;

		} else if (place instanceof TaxesPlace) {
			liElement = taxesGroup;

		} else if (place instanceof CustomersPlace) {
			liElement = customersGroup;

		} else if (place instanceof UsersPlace) {
			liElement = usersGroup;

		}

		if (oldGroup != null) {
			oldGroup.setClassName("");
		}

		if (liElement != null) {
			liElement.setClassName("active");
			oldGroup = liElement;
		}

	}

}
