package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.main.customers.CustomersActivity;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
import cbmarc.cigbill.client.main.invoices.InvoicesActivity;
import cbmarc.cigbill.client.main.invoices.InvoicesPlace;
import cbmarc.cigbill.client.main.payments.PaymentsActivity;
import cbmarc.cigbill.client.main.payments.PaymentsPlace;
import cbmarc.cigbill.client.main.products.ProductsActivity;
import cbmarc.cigbill.client.main.products.ProductsPlace;
import cbmarc.cigbill.client.main.taxes.TaxesActivity;
import cbmarc.cigbill.client.main.taxes.TaxesPlace;
import cbmarc.cigbill.client.main.users.UsersActivity;
import cbmarc.cigbill.client.main.users.UsersPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ContentActivityMapper implements ActivityMapper {
		
	@Inject
	private Provider<ActivityAsyncProxy<UsersActivity>> usersActivity;
	
	@Inject
	private Provider<ActivityAsyncProxy<InvoicesActivity>> invoicesActivity;
	
	@Inject
	private Provider<ActivityAsyncProxy<PaymentsActivity>> paymentsActivity;
	
	@Inject
	private Provider<ActivityAsyncProxy<ProductsActivity>> productsActivity;
	
	@Inject
	private Provider<ActivityAsyncProxy<TaxesActivity>> taxesActivity;
	
	@Inject
	private Provider<ActivityAsyncProxy<CustomersActivity>> customersActivity;

	@Override
	public Activity getActivity(Place place) {

		if (place instanceof UsersPlace) {
			return usersActivity.get();

		} else if (place instanceof CustomersPlace) {
			return customersActivity.get();

		} else if (place instanceof InvoicesPlace) {
			return invoicesActivity.get();

		} else if (place instanceof PaymentsPlace) {
			return paymentsActivity.get();

		} else if (place instanceof ProductsPlace) {
			return productsActivity.get();

		} else if (place instanceof TaxesPlace) {
			return taxesActivity.get();

		}

		return null;

	}
}
