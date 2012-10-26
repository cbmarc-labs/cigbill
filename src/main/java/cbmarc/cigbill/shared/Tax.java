package cbmarc.cigbill.shared;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface Tax {
	
	Long getId();
	void setId(Long id);
	
	@NotNull
	@Size(min = 1, max = 25)
	String getName();
	void setName(String name);
	
	@Size(max = 100)
	String getDescription();
	void setDescription(String description);

}
