package cbmarc.cigbill.client.main.users;

import com.google.gwt.i18n.client.Constants;

public interface UsersConstants extends Constants {
	
	// COLUMNS
	@DefaultStringValue("Login")
	String columnLogin();
	
	@DefaultStringValue("Created")
	String columnCreated();
	
	@DefaultStringValue("Updated")
	String columnUpdated();
	
	@DefaultStringValue("Active")
	String columnActive();
	
	// FORM
	@DefaultStringValue("Add user")
	String addLegendLabel();
	
	@DefaultStringValue("Edit user")
	String editLegendLabel();
	
	@DefaultStringValue("Login")
	String formLogin();
	
	@DefaultStringValue("Password")
	String formPassword();
	
	@DefaultStringValue("Confirm password")
	String formConfirmPassword();
	
	@DefaultStringValue("Sex")
	String formSex();
	
	@DefaultStringValue("Woman")
	String formSex1();
	
	@DefaultStringValue("Man")
	String formSex2();
	
	@DefaultStringValue("Other")
	String formSex3();

	@DefaultStringValue("Favorite color")
	String formFavoriteColor();
	
	@DefaultStringValue("White")
	String formFavoriteColor0();
	
	@DefaultStringValue("Black")
	String formFavoriteColor1();
	
	@DefaultStringValue("Red")
	String formFavoriteColor2();
	
	@DefaultStringValue("Blue")
	String formFavoriteColor3();
	
	@DefaultStringValue("Description")
	String formDescription();
	
	@DefaultStringValue("Active")
	String formActive();

}
