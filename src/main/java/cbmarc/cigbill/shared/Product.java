package cbmarc.cigbill.shared;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Product implements Model {
	
	private String id;
	
	@NotNull
	@Size(min = 2, max = 25)
	private String name;
	
	@Size(max = 100)
	private String description;

	@Digits(integer = 7, fraction=2)
	private Double price = 0.0;
	
	@Size(max = 250)
	private String notes;
	
	private String model;
	private Float length;
	private Float width;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
