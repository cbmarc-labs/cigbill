package cbmarc.cigbill.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {

    public static final Resources INSTANCE = GWT.create(Resources.class);
    
    @Source("assets/js/jquery-1.8.2.min.js")
    TextResource jquery();
    
    @Source("assets/js/jquery.textarea-expander.js")
    TextResource textareaExpander();
    
    @Source("assets/js/jquery.pnotify.min.js")
    TextResource pnotify();
    
    @Source("assets/js/bootstrap.min.js")
    TextResource bootstrap();
    
    @Source("assets/js/bootstrap-datepicker.js")
    TextResource datepicker();
}