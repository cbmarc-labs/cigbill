package cbmarc.cigbill.client.mvp;

import cbmarc.cigbill.client.auth.AuthPlace;
import cbmarc.cigbill.client.main.MainPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers( {
	MainPlace.Tokenizer.class,
	AuthPlace.Tokenizer.class,})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
