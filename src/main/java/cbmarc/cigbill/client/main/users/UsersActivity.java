/**
 * 
 */
package cbmarc.cigbill.client.main.users;

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
import cbmarc.cigbill.shared.User;

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
public class UsersActivity extends AbstractActivity implements
		UsersView.Presenter {

	private UsersConstants usersConstants = GWT.create(UsersConstants.class);
	private UsersServiceAsync service = GWT.create(UsersServiceImpl.class);

	@Inject
	private UsersView view;
	@Inject
	private PlaceController placeController;
	@Inject
	AppConstants appConstants;

	private SimpleBeanEditorDriver<User, ?> driver;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);

		driver = view.createEditorDriver();
		driver.edit(new User());

		String token = ((MainPlace) placeController.getWhere()).getToken();

		// String token[] = ((MainPlace)
		// placeController.getWhere()).getSplitToken();
		if (token.equals("add"))
			doAdd();

		else if (token.equals("edit"))
			doEdit(token);

		else
			doLoad();
	}

	/**
	 * Load all items to cellData view
	 */
	@Override
	public void doLoad() {
		view.showCellTablePanel();
		service.getAll(new AppAsyncCallback<List<User>>() {

			@Override
			public void onSuccess(List<User> result) {
				view.setList(result);

			}
		});
	}

	/**
	 * Show form and populate fields with editor driver
	 */
	@Override
	public void doAdd() {
		view.showFormPanel(usersConstants.addLegendLabel());

		driver.edit(new User());

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(String token) {
		if (token != null) {
			service.getById(token, new AppAsyncCallback<User>() {

				@Override
				public void onSuccess(User result) {
					if (result == null) {
						Scheduler.get().scheduleDeferred(
								new ScheduledCommand() {

									@Override
									public void execute() {
										goTo(new UsersPlace(""));

									}
								});

						new AppMessage(appConstants.itemNotFound(),
								AppMessage.ERROR);

					} else {
						view.showFormPanel(usersConstants.editLegendLabel());
						driver.edit(result);
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
		final User user = driver.flush();
		final String id = user.getId();

		if (validateForm(user)) {
			service.save(user, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(user);
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
	public void doDelete(Set<User> list) {
		service.delete(list, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				new AppMessage(appConstants.itemsDeleted(), AppMessage.SUCCESS);
				doLoad();

			}
		});

	}

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(User user) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<User>> violations = validator.validate(user,
				Default.class, ClientGroup.class);
		Boolean result = true;

		StringBuffer validationErrors = new StringBuffer();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<User> constraintViolation : violations) {

				String field = constraintViolation.getPropertyPath().toString();
				String message = constraintViolation.getMessage();

				if (validationErrors.length() != 0)
					validationErrors.append("<br>");

				validationErrors.append(field + " " + message);

				view.setFieldError(field, message);
			}

			result = false;
		}

		/*
		 * if (!user.getPassword().equals(user.getConfirmPassword())) {
		 * view.setFieldError("password", "Not matches");
		 * view.setFieldError("confirmPassword", "Not matches");
		 * 
		 * if (validationErrors.length() != 0) validationErrors.append("<br>");
		 * validationErrors
		 * .append("password and confirm password Not matches");
		 * 
		 * result = false; }
		 */

		if (result == false)
			view.setFormErrors(validationErrors.toString());

		return result;
	}

	/**
	 * go to new place
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public String mayStop() {
		if (driver.isDirty())
			return appConstants.formDiscardChanges();

		return null;
	}

}
