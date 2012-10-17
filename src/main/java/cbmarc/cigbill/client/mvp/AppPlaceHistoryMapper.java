package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.auth.AuthPlace;
import cbmarc.cigbill.client.main.customers.CustomersPlace;
import cbmarc.cigbill.client.main.users.UsersPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ AuthPlace.Tokenizer.class, UsersPlace.Tokenizer.class,
		CustomersPlace.Tokenizer.class })
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
