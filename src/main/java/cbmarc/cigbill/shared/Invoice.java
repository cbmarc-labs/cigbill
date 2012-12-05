package cbmarc.cigbill.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Invoice implements Model {

	private Long id;

	private Date created;

	@Size(max = 250)
	private String notes;

	@NotNull
	private Customer customer;
	
	private Date date;
	
	@Digits(integer = 7, fraction = 2)
	private Double tax;
	
	@Digits(integer = 7, fraction = 2)
	private Double discount;
	
	@Digits(integer = 7, fraction = 2)
	private Double shipping;

	private List<Product> items = new ArrayList<Product>();

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getShipping() {
		return shipping;
	}

	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	public List<Product> getItems() {
		return items;
	}

	public void setItems(List<Product> items) {
		this.items.clear();
		this.items.addAll(items);
	}

}
