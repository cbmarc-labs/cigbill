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
import cbmarc.cigbill.client.rpc.AppAsyncCallback;
import cbmarc.cigbill.client.ui.AppMessage;
import cbmarc.cigbill.shared.ClientGroup;
import cbmarc.cigbill.shared.Product;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author marc
 * 
 */
public class ProductsActivity extends AbstractActivity implements
		ProductsView.Presenter {

	private AppConstants appConstants = GWT.create(AppConstants.class);
	private ProductsConstants productsConstants = GWT.create(ProductsConstants.class);

	private ProductsServiceAsync service = GWT.create(ProductsServiceImpl.class);
	private ProductsView view;

	private SimpleBeanEditorDriver<Product, ?> driver;
	private Product product = null;

	/**
	 * start method
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = new ProductsViewImpl();
		view.setPresenter(this);
		panel.setWidget(view);

		driver = view.createEditorDriver();

		/*String token[] = ((MainPlace) place).getSplitToken();
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
		service.getAll(new AppAsyncCallback<List<Product>>() {

			@Override
			public void onSuccess(List<Product> result) {
				view.setList(result);

			}
		});
	}

	/**
	 * Show form and populate fields with editor driver
	 */
	@Override
	public void doAdd() {
		view.showFormPanel(productsConstants.addLegendLabel());

		product = new Product();
		driver.edit(product);

	}

	/**
	 * Show form, get item from service and populate with editor driver
	 * 
	 * @param token
	 */
	public void doEdit(String token) {
		if (token != null) {
			service.getById(token, new AppAsyncCallback<Product>() {

				@Override
				public void onSuccess(Product result) {
					if (result == null) {
						Scheduler.get().scheduleDeferred(
								new ScheduledCommand() {

									@Override
									public void execute() {
										//goTo(new MainPlace("products"));

									}
								});

						new AppMessage(appConstants.itemNotFound(),
								AppMessage.ERROR);

					} else {
						view.showFormPanel(productsConstants.editLegendLabel());
						product = result;
						driver.edit(product);
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
		product = driver.flush();
		final String id = product.getId();

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

	/**
	 * validateForm method
	 * 
	 * @return
	 */
	private boolean validateForm(Product product) {
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<Product>> violations = validator.validate(product,
				Default.class, ClientGroup.class);
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

	/**
	 * go to new place
	 */
	@Override
	public void goTo(Place place) {
		//clientFactory.getPlaceController().goTo(place);

	}

	@Override
	public String mayStop() {
		if (product != null)
			if (driver.isDirty())
				return appConstants.formDiscardChanges();

		return null;
	}

}
