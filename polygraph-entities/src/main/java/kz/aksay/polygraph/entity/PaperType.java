package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Check;

@Entity
@Table(name="paper_type", uniqueConstraints = {
		@UniqueConstraint(name="UNQ_PAPER_TYPE_NAME", columnNames="name")})
public class PaperType extends EntitySupport {
	
	private static final long serialVersionUID = 2151450425605008962L;
	
	@Column
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public static class DefaultNames {
		
		public static final String GLOSS = "Gloss"; 
		public static final String COL_PLUS = "Col+";
		public static final String SVET = "Svet";
		
		private static String[] defaultNames = new String[] {
			GLOSS,
			COL_PLUS,
			SVET
		};
		
		public static String[] all() {
			return defaultNames;
		}
	}

}
