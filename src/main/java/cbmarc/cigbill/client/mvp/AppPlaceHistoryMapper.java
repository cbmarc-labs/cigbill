package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.auth.AuthPlace;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
import cbmarc.cigbill.client.main.home.HomePlace;
import cbmarc.cigbill.client.main.invoices.InvoicesPlace;
import cbmarc.cigbill.client.main.payments.PaymentsPlace;
import cbmarc.cigbill.client.main.products.ProductsPlace;
import cbmarc.cigbill.client.main.taxes.TaxesPlace;
import cbmarc.cigbill.client.main.users.UsersPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ AuthPlace.Tokenizer.class, HomePlace.Tokenizer.class,
		UsersPlace.Tokenizer.class, CustomersPlace.Tokenizer.class,
		InvoicesPlace.Tokenizer.class, PaymentsPlace.Tokenizer.class,
		ProductsPlace.Tokenizer.class, TaxesPlace.Tokenizer.class })
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
