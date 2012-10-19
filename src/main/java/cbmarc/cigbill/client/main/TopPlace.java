package cbmarc.cigbill.client.main;

import com.google.gwt.place.shared.Place;

public class TopPlace extends Place {

	@Override
	public boolean equals(Object otherPlace) {
		return this == otherPlace
				|| (otherPlace != null && getClass() == otherPlace.getClass());
	}

}
