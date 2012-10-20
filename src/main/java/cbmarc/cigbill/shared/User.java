package cbmarc.cigbill.shared;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class User implements Model {

	private Long id;

	@NotNull
	@Size(min = 2, max = 25, message = "{custom.login.size.message}")
	private String login;

	@NotNull
	@Size(min = 2, max = 25)
	private String password;

	@Size(min = 1)
	private String sex;

	private List<String> favoriteColor;

	@Size(max = 250)
	private String description;

	private Boolean active = true;

	private Date created;

	private Date updated;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the favoriteColor
	 */
	public List<String> getFavoriteColor() {
		return favoriteColor;
	}

	/**
	 * @param favoriteColor
	 *            the favoriteColor to set
	 */
	public void setFavoriteColor(List<String> favoriteColor) {
		this.favoriteColor = favoriteColor;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the lastUpdated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
