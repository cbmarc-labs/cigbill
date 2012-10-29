/**
 * 
 */
package cbmarc.cigbill.client.main.products;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import cbmarc.cigbill.client.i18n.AppConstants;
import cbmarc.cigbill.client.main.MainPlace;
import cbmarc.cigbill.client.main.taxes.TaxesServiceAsync;
import cbmarc.cigbill.client.main.taxes.TaxesServiceImpl;
import cbmarc.cigbill.client.main.users.UsersPlace;
import cbmarc.cigbill.client.rpc.AppAsyncCallback;
import cbmarc.cigbill.client.ui.AppMessage;
import cbmarc.cigbill.shared.ClientGroup;
import cbmarc.cigbill.shared.Customer;
import cbmarc.cigbill.shared.Product;
import cbmarc.cigbill.shared.Tax;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author marc
 * 
 */
@Singleton
public class ProductsActivity extends AbstractActivity implements
		ProductsView.Presenter {

	private ProductsConstants productsConstants = GWT
			.create(ProductsConstants.class);
	private ProductsServiceAsync service = GWT
			.create(ProductsServiceImpl.class);
	private TaxesServiceAsync taxesService = GWT
			.create(TaxesServiceImpl.class);

	@Inject
	private ProductsView view;
	@Inject
	private AppConstants appConstants;
	@Inject
	private PlaceController placeController;

	private SimpleBeanEditorDriver<Product, ?> driver;

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
				goTo(new ProductsPlace());

			} else {
				try {
					doEdit(Long.parseLong(token[1]));
				} catch (Exception e) {

					goTo(new ProductsPlace());
				}
			}

		} else if (token[0].isEmpty()) {
			doLoad();

		} else {
			goTo(new ProductsPlace());
		}

	}

	/**
	 * Load all items to cellData view
	 */
	@Override
	public void doLoad() {
		view.showCellTablePanel();
		service.getAll(new AppAsyncCallback<List<Product>>() {

			@Override
			public void onSuccess(List<Product> result) {
				view.setList(result);

			}
		});
	}

	@Override
	public void doLoadTaxes() {
		taxesService.getAll(new AppAsyncCallback<List<Tax>>() {

			@Override
			public void onSuccess(List<Tax> result) {
				view.setTaxList(result);

			}
		});

	}

	/**
	 * Show form and populate fields with editor driver
	 */
	@Override
	public void doAdd() {
		view.showFormPanel(productsConstants.addLegendLabel());
		view.getFormDeleteButton().setVisible(false);

		driver.edit(new Product());
		
		doLoadTaxes();

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(Long id) {
		view.getFormDeleteButton().setVisible(true);
		service.getById(id, new AppAsyncCallback<Product>() {

			@Override
			public void onSuccess(Product result) {
				if (result == null) {
					goTo(new ProductsPlace());

					new AppMessage(appConstants.itemNotFound(),
							AppMessage.ERROR);

				} else {
					view.showFormPanel(productsConstants.editLegendLabel());
					driver.edit(result);
					
					doLoadTaxes();
				}

			}
		});
	}

	/**
	 * get fields with editor driver and call service
	 */
	@Override
	public void doSave() {
		final Product product = driver.flush();
		final Long id = product.getId();

		if (validateForm(product)) {
			service.save(product, new AppAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					if (id == null) {
						doAdd();

					} else {
						driver.edit(product);
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
	public void doDelete(Set<Product> list) {
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
		final Product product = driver.flush();
		service.delete(product, new AppAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				goTo(new ProductsPlace());

				new AppMessage(appConstants.itemsDeleted(), AppMessage.SUCCESS);

			}
		});

	}

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(Product product) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Product>> violations = validator.validate(
				product, Default.class, ClientGroup.class);
		Boolean result = true;

		StringBuffer validationErrors = new StringBuffer();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<Product> constraintViolation : violations) {

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
