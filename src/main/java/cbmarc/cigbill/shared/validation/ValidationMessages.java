package cbmarc.cigbill.shared.validation;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/*
 * Define ValidationMessages_xx.properties for more languages
 */
public interface ValidationMessages extends ConstantsWithLookup {
	
	@DefaultStringValue("must be between {min} and {max} characters long.")
    @Key("custom.login.size.message")
    String custom_login_size_message();

}
