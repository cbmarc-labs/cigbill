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

import cbmarc.cigbill.client.i18n.AppConstants;
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

	private CustomersConstants customersConstants = GWT
			.create(CustomersConstants.class);
	private CustomersServiceAsync service = GWT
			.create(CustomersServiceImpl.class);

	@Inject
	private CustomersView view;
	@Inject
	private AppConstants appConstants;
	@Inject
	private PlaceController placeController;

	private SimpleBeanEditorDriver<Customer, ?> driver;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);

		driver = view.createEditorDriver();
		driver.edit(new Customer());

		initialize();
	}

	private void initialize() {
		String token[] = ((MainPlace) placeController.getWhere())
				.getSplitToken();

		if (token[0].equals("add")) {
			doAdd();

		} else if (token[0].equals("edit")) {
			if (token[1] == null) {
				goTo(new CustomersPlace());

			} else {
				doEdit(token[1]);
			}

		} else if (token[0].isEmpty()) {
			doLoad();

		} else {
			goTo(new CustomersPlace());
		}

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
		view.showFormPanel(customersConstants.addLegendLabel());
		view.getFormDeleteButton().setVisible(false);

		driver.edit(new Customer());

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(String token) {
		view.getFormDeleteButton().setVisible(true);
		service.getById(token, new AppAsyncCallback<Customer>() {

			@Override
			public void onSuccess(Customer result) {
				if (result == null) {
					goTo(new CustomersPlace());

					new AppMessage(appConstants.itemNotFound(),
							AppMessage.ERROR);

				} else {
					view.showFormPanel(customersConstants.editLegendLabel());
					driver.edit(result);
				}

			}
		});
	}

	/**
	 * get fields with editor driver and call service
	 */
	@Override
	public void doSave() {
		final Customer customer = driver.flush();
		final String id = customer.getId();

		if (validateForm(customer)) {
			service.save(customer, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(customer);
					}

					new AppMessage(appConstants.itemSaved(), AppMessage.SUCCESS);

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
		service.delete(list, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				new AppMessage(appConstants.itemsDeleted(), AppMessage.SUCCESS);
				doLoad();

			}
		});

	}

	@Override
	public void doDelete() {
		final Customer customer = driver.flush();
		service.delete(customer, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				goTo(new CustomersPlace());

				new AppMessage(appConstants.itemsDeleted(), AppMessage.SUCCESS);

			}
		});

	}

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(Customer customer) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Customer>> violations = validator.validate(
				customer, Default.class, ClientGroup.class);
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

	@Override
	public String mayStop() {
		if (driver.isDirty())
			return appConstants.formDiscardChanges();

		return null;
	}

	@Override
	public void goTo(final Place place) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				placeController.goTo(place);

			}
		});

	}

}
