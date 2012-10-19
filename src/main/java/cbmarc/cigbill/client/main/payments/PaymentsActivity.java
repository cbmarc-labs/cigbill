/**
 * 
 */
package cbmarc.cigbill.client.main.payments;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.rpc.AppAsyncCallback;
import cbmarc.cigbill.client.ui.AppMessage;
import cbmarc.cigbill.shared.ClientGroup;
import cbmarc.cigbill.shared.Payment;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

/**
 * @author marc
 * 
 */
public class PaymentsActivity extends AbstractActivity implements
		PaymentsView.Presenter {

	private PaymentsConstants paymentsConstants = GWT
			.create(PaymentsConstants.class);
	private PaymentsServiceAsync service = GWT
			.create(PaymentsServiceImpl.class);

	@Inject
	private PaymentsView view;
	@Inject
	private AppConstants appConstants;
	@Inject
	private PlaceController placeController;

	private SimpleBeanEditorDriver<Payment, ?> driver;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);

		driver = view.createEditorDriver();
		driver.edit(new Payment());

		initialize();
	}

	private void initialize() {
		String token[] = ((MainPlace) placeController.getWhere())
				.getSplitToken();

		if (token[0].equals("add")) {
			doAdd();

		} else if (token[0].equals("edit")) {
			if (token[1] == null) {
				goTo(new PaymentsPlace());

			} else {
				doEdit(token[1]);
			}

		} else if (token[0].isEmpty()) {
			doLoad();

		} else {
			goTo(new PaymentsPlace());
		}

	}

	/**
	 * Load all items to cellData view
	 */
	@Override
	public void doLoad() {
		view.showCellTablePanel();
		service.getAll(new AppAsyncCallback<List<Payment>>() {

			@Override
			public void onSuccess(List<Payment> result) {
				view.setList(result);

			}
		});
	}

	/**
	 * Show form and populate fields with editor driver
	 */
	@Override
	public void doAdd() {
		view.showFormPanel(paymentsConstants.addLegendLabel());
		view.getFormDeleteButton().setVisible(false);

		driver.edit(new Payment());

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(String token) {
		view.getFormDeleteButton().setVisible(true);
		service.getById(token, new AppAsyncCallback<Payment>() {

			@Override
			public void onSuccess(Payment result) {
				if (result == null) {
					goTo(new PaymentsPlace());

					new AppMessage(appConstants.itemNotFound(),
							AppMessage.ERROR);

				} else {
					view.showFormPanel(paymentsConstants.editLegendLabel());
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
		final Payment payment = driver.flush();
		final String id = payment.getId();

		if (validateForm(payment)) {
			service.save(payment, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(payment);
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
	public void doDelete(Set<Payment> list) {
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
		final Payment payment = driver.flush();
		service.delete(payment, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				goTo(new PaymentsPlace());

				new AppMessage(appConstants.itemsDeleted(), AppMessage.SUCCESS);

			}
		});

	}

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(Payment payment) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Payment>> violations = validator.validate(
				payment, Default.class, ClientGroup.class);
		Boolean result = true;

		StringBuffer validationErrors = new StringBuffer();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<Payment> constraintViolation : violations) {

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
