package cbmarc.cigbill.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface AppResources extends ClientBundle {

    public static final AppResources INSTANCE = GWT.create(AppResources.class);

    // GWT CssResource does not support @media
    //@NotStrict
    @Source("style.css")
    TextResource css();
}