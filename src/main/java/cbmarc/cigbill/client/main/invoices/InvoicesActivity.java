/**
 * 
 */
package cbmarc.cigbill.client.main.invoices;

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
import cbmarc.cigbill.shared.Invoice;

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
public class InvoicesActivity extends AbstractActivity implements
		InvoicesView.Presenter {

	private AppConstants appConstants = GWT.create(AppConstants.class);
	private InvoicesConstants taxesConstants = GWT.create(InvoicesConstants.class);

	private InvoicesServiceAsync service = GWT.create(InvoicesServiceImpl.class);
	
	@Inject
	private InvoicesView view;
	@Inject
	private PlaceController placeController;

	private SimpleBeanEditorDriver<Invoice, ?> driver;
	private Invoice entity = null;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);

		driver = view.createEditorDriver();
		
		String token = ((MainPlace) placeController.getWhere()).getToken();

		//String token[] = ((MainPlace) place).getSplitToken();
		if (token.equals("add")) {
			doAdd();
			
		//} else if (token.equals("edit")) {
		//	doEdit(token);
			
		} else {
			doLoad();
			
		}
	}

	/**
	 * Load all items to cellData view
	 */
	@Override
	public void doLoad() {
		view.showCellTablePanel();
		service.getAll(new AppAsyncCallback<List<Invoice>>() {

			@Override
			public void onSuccess(List<Invoice> result) {
				view.setList(result);

			}
		});
	}

	/**
	 * Show form and populate fields with editor driver
	 */
	@Override
	public void doAdd() {
		view.showFormPanel(taxesConstants.addLegendLabel());

		entity = new Invoice();
		driver.edit(entity);

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(String token) {
		if (token != null) {
			service.getById(token, new AppAsyncCallback<Invoice>() {

				@Override
				public void onSuccess(Invoice result) {
					if (result == null) {
						Scheduler.get().scheduleDeferred(
								new ScheduledCommand() {

									@Override
									public void execute() {
										//goTo(new MainPlace("taxes"));

									}
								});

						new AppMessage(appConstants.itemNotFound(),
								AppMessage.ERROR);

					} else {
						view.showFormPanel(taxesConstants.editLegendLabel());
						entity = result;
						driver.edit(entity);
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
		entity = driver.flush();
		final String id = entity.getId();

		if (validateForm()) {
			service.save(entity, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(entity);
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
	public void doDelete(Set<Invoice> list) {
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
	private boolean validateForm() {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Invoice>> violations = validator.validate(entity,
				Default.class, ClientGroup.class);
		Boolean result = true;

		StringBuffer validationErrors = new StringBuffer();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<Invoice> constraintViolation : violations) {

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
		if (entity != null)
			if (driver.isDirty())
				return appConstants.formDiscardChanges();

		return null;
	}

}
