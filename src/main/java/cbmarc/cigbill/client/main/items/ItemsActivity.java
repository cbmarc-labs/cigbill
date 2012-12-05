/**
 * 
 */
package cbmarc.cigbill.client.main.items;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.rpc.AppAsyncCallback;
import cbmarc.cigbill.client.ui.AppNotify;
import cbmarc.cigbill.shared.ClientGroup;
import cbmarc.cigbill.shared.Item;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author marc
 * 
 */
@Singleton
public class ItemsActivity extends AbstractActivity implements
		ItemsView.Presenter {

	private ItemsConstants itemsConstants = GWT.create(ItemsConstants.class);

	private ItemsServiceAsync service = GWT.create(ItemsServiceImpl.class);

	@Inject
	private ItemsView view;
	
	@Inject
	private AppConstants appConstants;
	
	@Inject
	private PlaceController placeController;

	private SimpleBeanEditorDriver<Item, ?> driver;

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
			if (token[1] == null) {
				goTo(new ItemsPlace());

			} else {
				try {
					doEdit(Long.parseLong(token[1]));
				} catch (Exception e) {

					goTo(new ItemsPlace());
				}
			}

		} else if (token[0].isEmpty()) {
			doLoad();

		} else {
			goTo(new ItemsPlace());
		}

	}

	/**
	 * Load all items to cellData view
	 */
	@Override
	public void doLoad() {
		view.showCellTablePanel();
		service.getAll(new AppAsyncCallback<List<Item>>() {

			@Override
			public void onSuccess(List<Item> result) {
				view.setList(result);

			}
		});
	}

	/**
	 * Show form and populate fields with editor driver
	 */
	@Override
	public void doAdd() {
		view.showFormPanel(itemsConstants.addLegendLabel());
		view.setFormDeleteButtonVisible(false);

		driver.edit(new Item());

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(Long id) {
		view.setFormDeleteButtonVisible(true);
		service.getById(id, new AppAsyncCallback<Item>() {

			@Override
			public void onSuccess(Item result) {
				if (result == null) {
					goTo(new ItemsPlace());

					AppNotify.error(appConstants.itemNotFound());

				} else {
					view.showFormPanel(itemsConstants.editLegendLabel());
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
		final Item item = driver.flush();
		final Long id = item.getId();

		if (validateForm(item)) {
			service.save(item, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(item);
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
	public void doDelete(Set<Item> list) {
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
		final Item item = driver.flush();
		service.delete(item, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				goTo(new ItemsPlace());

				AppNotify.success(appConstants.itemsDeleted());

			}
		});

	}

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(Item item) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Item>> violations = validator.validate(item,
				Default.class, ClientGroup.class);
		Boolean result = true;

		StringBuffer validationErrors = new StringBuffer();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<Item> constraintViolation : violations) {

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
