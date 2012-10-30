package cbmarc.cigbill.client.auth;

import com.google.gwt.i18n.client.Constants;

public interface AuthConstants extends Constants {
		
	// FORM
	@DefaultStringValue("Login")
	String legendLabel();
	
	@DefaultStringValue("Login")
	String formLogin();
	
	@DefaultStringValue("Password")
	String formPassword();
	
	@DefaultStringValue("Remenber Me")
	String formRememberMe();

}
