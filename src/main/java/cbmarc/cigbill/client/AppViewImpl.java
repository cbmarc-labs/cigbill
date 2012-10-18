package cbmarc.cigbill.client;

import cbmarc.cigbill.client.mvp.CachingTopActivityMapper;
import cbmarc.cigbill.client.mvp.ContentActivityMapper;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class AppViewImpl extends Composite implements AppView {

	private static RootViewImplUiBinder uiBinder = GWT
			.create(RootViewImplUiBinder.class);

	interface RootViewImplUiBinder extends UiBinder<Widget, AppViewImpl> {
	}

	@UiField
	SimplePanel topPanel;
	@UiField
	SimplePanel contentPanel;

	@Inject
	public AppViewImpl(	CachingTopActivityMapper cachingTopActivityMapper,
			ContentActivityMapper contentActivityMapper, EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));

		ActivityManager cachingTopActivityManager = new ActivityManager(
				cachingTopActivityMapper, eventBus);
		cachingTopActivityManager.setDisplay(topPanel);
		
		ActivityManager contentActivityManager = new ActivityManager(
				contentActivityMapper, eventBus);
		contentActivityManager.setDisplay(contentPanel);
	}

}
