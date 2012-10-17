package cbmarc.cigbill.shared.validation;

import javax.validation.Validator;
import javax.validation.groups.Default;

import cbmarc.cigbill.shared.ClientGroup;
import cbmarc.cigbill.shared.Customer;
import cbmarc.cigbill.shared.Invoice;
import cbmarc.cigbill.shared.Payment;
import cbmarc.cigbill.shared.Product;
import cbmarc.cigbill.shared.Tax;
import cbmarc.cigbill.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

public final class AppValidatorFactory extends AbstractGwtValidatorFactory {

	/**
	 * Validator marker for the Validation Sample project. Only the classes
	 * listed in the {@link GwtValidation} annotation can be validated.
	 */
	@GwtValidation(value = { User.class, Customer.class, Product.class,
			Tax.class, Payment.class, Invoice.class }, groups = { Default.class,
			ClientGroup.class })
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}

}
