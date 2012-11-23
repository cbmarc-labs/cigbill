package cbmarc.cigbill.client;

import cbmarc.cigbill.client.mvp.AuthActivityMapper;
import cbmarc.cigbill.client.mvp.CachingBreadcrumbActivityMapper;
import cbmarc.cigbill.client.mvp.CachingNavActivityMapper;
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
	SimplePanel topPanel, authPanel, navPanel, breadcrumbPanel, contentPanel;

	@Inject
	public AppViewImpl(CachingTopActivityMapper cachingTopActivityMapper,
			CachingNavActivityMapper cachingNavActivityMapper,
			AuthActivityMapper authActivityMapper,
			CachingBreadcrumbActivityMapper cachingBreadcrumbActivityMapper,
			ContentActivityMapper contentActivityMapper, EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));

		ActivityManager topActivityManager = new ActivityManager(
				cachingTopActivityMapper, eventBus);
		topActivityManager.setDisplay(topPanel);

		ActivityManager authActivityManager = new ActivityManager(
				authActivityMapper, eventBus);
		authActivityManager.setDisplay(authPanel);

		ActivityManager navActivityManager = new ActivityManager(
				cachingNavActivityMapper, eventBus);
		navActivityManager.setDisplay(navPanel);

		ActivityManager breadcrumbActivityManager = new ActivityManager(
				cachingBreadcrumbActivityMapper, eventBus);
		breadcrumbActivityManager.setDisplay(breadcrumbPanel);

		ActivityManager contentActivityManager = new ActivityManager(
				contentActivityMapper, eventBus);
		contentActivityManager.setDisplay(contentPanel);
	}

}
