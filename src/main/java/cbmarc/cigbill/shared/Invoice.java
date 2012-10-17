package cbmarc.cigbill.shared;

import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Invoice implements Model {
	
	private String id;
	
	@Size(max = 250)
	private String notes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
