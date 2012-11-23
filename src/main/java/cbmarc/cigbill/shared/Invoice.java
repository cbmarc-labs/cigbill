package cbmarc.cigbill.shared;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Invoice implements Model {

	private Long id;

	private Date created;

	@Size(max = 250)
	private String notes;

	private Customer customer;

	private List<Product> products;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
