package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Check;

@Entity
@Table(name="employee_type")
@Check(constraints="name=UPPER(name)")
public class EmployeeType extends EntitySupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2734620163914064767L;
	
	@Column(nullable=false, unique=true)
	@Size(min=2, max=32)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static final class DefaultNames {
		public static final String DESIGNER = "ДИЗАЙНЕР";
		public static final String DIRECTOR = "ДИРЕКТОР";
		
		public static String[] all() {
			return new String[] {
					DESIGNER,
					DIRECTOR
			};
		}
	}
}
