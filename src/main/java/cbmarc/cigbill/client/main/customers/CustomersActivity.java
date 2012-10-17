/**
 * 
 */
package cbmarc.cigbill.client.main.customers;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import cbmarc.cigbill.client.i18n.AppMessages;
import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.rpc.AppAsyncCallback;
import cbmarc.cigbill.client.ui.AppMessage;
import cbmarc.cigbill.shared.ClientGroup;
import cbmarc.cigbill.shared.Customer;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author marc
 * 
 */
@Singleton
public class CustomersActivity extends AbstractActivity implements
		CustomersView.Presenter {

	private AppMessages messages;
	private CustomersConstants customersConstants = GWT.create(CustomersConstants.class);

	private CustomersServiceAsync service = GWT.create(CustomersServiceImpl.class);

	private SimpleBeanEditorDriver<Customer, ?> driver;
	private Customer customer = null;
	
	@Inject
	private CustomersView view;
	@Inject
	private PlaceController placeController;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Internationalization
		//messages = clientFactory.getMessages();

		//view = new CustomersViewImpl();
		view.setPresenter(this);
		panel.setWidget(view);

		/*driver = view.createEditorDriver();
		driver.edit(customer);

		String token[] = ((MainPlace) place).getSplitToken();
		if (token[1].equals("add"))
			doAdd();

		else if (token[1].equals("edit"))
			doEdit(token[2]);

		else
			doLoad();*/
	}

	/**
	 * Load all items to cellData view
	 */
	@Override
	public void doLoad() {
		view.showCellTablePanel();
		service.getAll(new AppAsyncCallback<List<Customer>>() {

			@Override
			public void onSuccess(List<Customer> result) {
				view.setList(result);

			}
		});
	}

	/**
	 * Show form and populate fields with editor driver
	 */
	@Override
	public void doAdd() {
		customer = new Customer();
		driver.edit(customer);

		view.showFormPanel(customersConstants.addLegendLabel());

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(String token) {
		if (token != null) {
			service.getById(token, new AppAsyncCallback<Customer>() {

				@Override
				public void onSuccess(Customer result) {
					if (result == null) {
						Scheduler.get().scheduleDeferred(
								new ScheduledCommand() {

									@Override
									public void execute() {
										//goTo(new MainPlace("customers"));

									}
								});

						new AppMessage("Item not found", AppMessage.ERROR);

					} else {
						view.showFormPanel(customersConstants.editLegendLabel());
						customer = result;
						driver.edit(customer);
					}

				}
			});
		} else {
			doLoad();
		}
	}

	/**
	 * get fields with editor driver and call service
	 */
	@Override
	public void doSave() {
		customer = driver.flush();
		final String id = customer.getId();
		
		if (validateForm(customer)) {
			service.save(customer, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					String msg = "Item updated";

					if (id == null) {
						msg = "Item saved";
						doAdd();

					} else {
						// Necessary for clear the dirty
						driver.edit(customer);
					}

					new AppMessage(msg, AppMessage.SUCCESS);

				}
			});
		}
	}

	/**
	 * Call service for delete items
	 * 
	 * @param list
	 */
	@Override
	public void doDelete(Set<Customer> list) {
		final Integer total = list.size();

		service.delete(list, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				new AppMessage("items deleted", AppMessage.SUCCESS);
				doLoad();

			}
		});

	}

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(Customer item) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Customer>> violations = validator.validate(item,
				Default.class, ClientGroup.class);
		Boolean result = true;

		StringBuffer validationErrors = new StringBuffer();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<Customer> constraintViolation : violations) {

				String field = constraintViolation.getPropertyPath().toString();
				String message = constraintViolation.getMessage();

				if (validationErrors.length() != 0)
					validationErrors.append("<br>");

				validationErrors.append(field + " " + message);

				view.setFieldError(field, message);
			}

			result = false;
		}

		if (result == false)
			view.setFormErrors(validationErrors.toString());

		return result;
	}

	/**
	 * go to new place
	 */
	@Override
	public void goTo(Place place) {
		//clientFactory.getPlaceController().goTo(place);

	}

	@Override
	public String mayStop() {
		if (customer != null)
			if (driver.isDirty())
				return "Have you finished your work ?";

		return null;
	}

}
