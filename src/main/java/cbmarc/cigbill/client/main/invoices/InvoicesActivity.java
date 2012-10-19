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
import cbmarc.cigbill.client.main.products.ProductsServiceAsync;
import cbmarc.cigbill.client.main.products.ProductsServiceImpl;
import cbmarc.cigbill.client.rpc.AppAsyncCallback;
import cbmarc.cigbill.client.ui.AppMessage;
import cbmarc.cigbill.shared.ClientGroup;
import cbmarc.cigbill.shared.Invoice;
import cbmarc.cigbill.shared.Product;

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

	private InvoicesConstants taxesConstants = GWT
			.create(InvoicesConstants.class);
	private InvoicesServiceAsync service = GWT
			.create(InvoicesServiceImpl.class);
	private ProductsServiceAsync serviceProducts = GWT
			.create(ProductsServiceImpl.class);

	@Inject
	private InvoicesView view;
	@Inject
	private AppConstants appConstants;
	@Inject
	private PlaceController placeController;

	private SimpleBeanEditorDriver<Invoice, ?> driver;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);

		driver = view.createEditorDriver();
		driver.edit(new Invoice());

		initialize();
	}

	private void initialize() {
		String token[] = ((MainPlace) placeController.getWhere())
				.getSplitToken();

		if (token[0].equals("add")) {
			doAdd();

		} else if (token[0].equals("edit")) {
			if (token[1] == null) {
				goTo(new InvoicesPlace());

			} else {
				doEdit(token[1]);
			}

		} else if (token[0].isEmpty()) {
			doLoad();

		} else {
			goTo(new InvoicesPlace());
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
		view.getFormDeleteButton().setVisible(false);

		driver.edit(new Invoice());
		
		serviceProducts.getAll(new AppAsyncCallback<List<Product>>(){

			@Override
			public void onSuccess(List<Product> result) {
				view.setListProduct(result);
				
			}});

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(String token) {
		view.getFormDeleteButton().setVisible(true);
		service.getById(token, new AppAsyncCallback<Invoice>() {

			@Override
			public void onSuccess(Invoice result) {
				if (result == null) {
					goTo(new InvoicesPlace());

					new AppMessage(appConstants.itemNotFound(),
							AppMessage.ERROR);

				} else {
					view.showFormPanel(taxesConstants.editLegendLabel());
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
		final Invoice invoice = driver.flush();
		final String id = invoice.getId();

		if (validateForm(invoice)) {
			service.save(invoice, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(invoice);
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

	@Override
	public void doDelete() {
		final Invoice invoice = driver.flush();
		service.delete(invoice, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				goTo(new InvoicesPlace());

				new AppMessage(appConstants.itemsDeleted(), AppMessage.SUCCESS);

			}
		});

	}

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(Invoice invoice) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Invoice>> violations = validator.validate(
				invoice, Default.class, ClientGroup.class);
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
