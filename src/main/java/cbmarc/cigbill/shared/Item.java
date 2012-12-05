package cbmarc.cigbill.shared;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Item implements Model {

	private Long id;

	@NotNull
	@Size(min = 2, max = 100)
	private String description;
	
	@Digits(integer = 7, fraction = 2)
	private Double quantity = 1d;

	@Digits(integer = 7, fraction = 2)
	private Double price = 0d;

	@Size(max = 250)
	private String notes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
