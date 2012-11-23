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
import cbmarc.cigbill.client.ui.AppNotify;
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
	private AppConstants appConstants;
	
	@Inject
	protected PlaceController placeController;

	protected SimpleBeanEditorDriver<User, ?> driver;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);

		driver = view.createEditorDriver();
		driver.edit(null);

		initialize();
	}

	private void initialize() {
		String token[] = ((MainPlace) placeController.getWhere())
				.getSplitToken();

		if (token[0].equals("add")) {
			doAdd();

		} else if (token[0].equals("edit")) {
			try {
				doEdit(Long.parseLong(token[1]));
			} catch (Exception e) {

				goTo(new UsersPlace());
			}

		} else if (token[0].isEmpty()) {
			doLoad();

		} else {
			goTo(new UsersPlace());
		}

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
		view.setFormDeleteButtonVisible(false);

		driver.edit(new User());

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(Long id) throws Exception {
		view.setFormDeleteButtonVisible(true);
		service.getById(id, new AppAsyncCallback<User>() {

			@Override
			public void onSuccess(User result) {
				if (result == null) {
					goTo(new UsersPlace());

					AppNotify.error(appConstants.itemNotFound());

				} else {
					view.showFormPanel(usersConstants.editLegendLabel());
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
		final User user = driver.flush();
		final Long id = user.getId();

		if (validateForm(user)) {
			service.save(user, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(user);
					}

					AppNotify.success(appConstants.itemSaved());

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
				AppNotify.success(appConstants.itemsDeleted());
				doLoad();

			}
		});

	}

	@Override
	public void doDelete() {
		final User user = driver.flush();
		service.delete(user, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				goTo(new UsersPlace());

				AppNotify.success(appConstants.itemsDeleted());

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
