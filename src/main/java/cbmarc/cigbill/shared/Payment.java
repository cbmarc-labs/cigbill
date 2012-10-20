package cbmarc.cigbill.shared;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Payment implements Model {
	
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 25)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void copy(Payment payment) {
		setId(payment.getId());
		setName(payment.getName());
	}
	
	public Payment clone() {
		Payment payment = new Payment();
		
		payment.setId(getId());
		payment.setName(getName());
		
		return payment;
	}
	
}
