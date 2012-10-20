package cbmarc.cigbill.shared;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Customer implements Model {
	
	private Long id;
	
	@NotNull
	@Size(min = 2, max = 25)
	private String name;
	
	@Size(max = 50)
	private String address;
	
	@Size(max = 50)
	private String city;
	
	@Size(max = 50)
	private String state;
	
	@Size(max = 50)
	private String country;
	
	@Size(max = 50)
	private String phone;
	
	@Pattern(regexp="^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$")
	private String email;
	
	@Size(max = 250)
	private String notes;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
}
