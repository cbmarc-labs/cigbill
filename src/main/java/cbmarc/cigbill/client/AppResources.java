package cbmarc.cigbill.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface AppResources extends ClientBundle {

	public static final AppResources INSTANCE = GWT.create(AppResources.class);

	@NotStrict
	@Source("style.css")
	CssResource css();
}