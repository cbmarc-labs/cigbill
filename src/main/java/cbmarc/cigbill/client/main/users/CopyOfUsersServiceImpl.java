package cbmarc.cigbill.client.main.users;


public class CopyOfUsersServiceImpl { /*implements UsersServiceAsync {
	
	public static String PATH = "http://localhost/cigbill/src/main/webapp/app.php/";
	public static String PATH_GETALL = PATH + "people/users/getall";
	public static String PATH_SAVE = PATH + "people/users/save";
	
	interface Bean {
		String getLogin();
		void setLogin(String login);
		
		String getPassword();
		void setPassword(String password);
		
		String getSex();
		void setSex(String sex);
		
		List<String> getFavoriteColor();
		void setFavoriteColor(List<String> favoriteColor);
		
		String getDescription();
		void setDescription(String description);
		
		Boolean getActive();
		void setActive(Boolean active);
		
		Date getCreated();
		void setCreated(Date created);

		Date getUpdated();
		void setUpdated(Date updated);
	}
	
	// {"data":[{"login":"admin","password":"21232 ...
	interface BeanList {
		List<Bean> getData();
	}
	
	interface BeanFactory extends AutoBeanFactory {
		AutoBean<BeanList> getall();
		AutoBean<Bean> bean();
	}
	
	BeanFactory beanFactory = GWT.create(BeanFactory.class);
	
	//GWT.getModuleBaseURL()
		
	public void getAll(final AsyncCallback<List<User>> callback) {
		try {
			new JSONRequest(PATH_GETALL, null, new JSONRequestCallback(){
	
				@Override
				public void onSuccess(Request request, String data) {
					try {
						BeanList beanList = AutoBeanCodex.decode(
								beanFactory, BeanList.class, data).as();
						
						List<Bean> beans = beanList.getData();
						
						List<User> users = new ArrayList<User>();
						for(Bean bean: beans) {
							users.add(makeUser(bean));
						}
						
						callback.onSuccess(users);
						
					} catch(Exception e) {
						callback.onFailure(e);
					}
					
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
					
				}});
		} catch (Exception e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void save(User user, final AsyncCallback<Void> callback) {		
		Bean bean = makeBean(user);
		AutoBean<Bean> autoBean = AutoBeanUtils.getAutoBean(bean);		
		String json = AutoBeanCodex.encode(autoBean).getPayload();
		
		Window.alert(json);
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(PATH_SAVE));
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("login")).append("=").append(URL.encode(user.getLogin()));
		postData.append("&");
		postData.append(URL.encode("password")).append("=").append(URL.encode(user.getPassword()));
		postData.append("&");
		postData.append(URL.encode("active")).append("=").append(user.getActive());
		
		Window.alert(postData.toString());
				
		try {
			Request response = builder.sendRequest(postData.toString(), 
					new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					Window.alert("text => " + response.getText());
					
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
					
				}
				
			});
		} catch (RequestException e) {
			callback.onFailure(e);
		}
				
		try {
			new JSONRequest(PATH_SAVE, json, new JSONRequestCallback(){

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
					
				}

				@Override
				public void onSuccess(Request request, String data) {
					callback.onSuccess(null);
					
				}});
		} catch(Exception e) {
			callback.onFailure(e);
		}
		
	}
	
	// Make User from Bean
	private User makeUser(Bean bean) {
		User user = new User();
		
		user.setLogin(bean.getLogin());
		user.setPassword(bean.getPassword());
		user.setSex(bean.getSex());
		user.setFavoriteColor(bean.getFavoriteColor());
		user.setDescription(bean.getDescription());
		user.setActive(bean.getActive());
		user.setCreated(bean.getCreated());
		user.setUpdated(bean.getUpdated());
		
		return user;
	}
	
	// Make Bean from User
	private Bean makeBean(User user) {
		Bean bean = beanFactory.bean().as();
		
		bean.setLogin(user.getLogin());
		bean.setPassword(user.getPassword());
		bean.setSex(user.getSex());
		//bean.setFavoriteColor(user.getFavoriteColor());
		bean.setDescription(user.getDescription());
		bean.setActive(user.getActive());
		
		return bean;
	}*/

}
