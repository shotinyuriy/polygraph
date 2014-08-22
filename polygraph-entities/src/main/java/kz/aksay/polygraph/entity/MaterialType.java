package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Check;

@Entity
@Table(name="material_type")
@Check(constraints="name=UPPER(name)")
public class MaterialType extends EntitySupport {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2151450425605008962L;
	
	@Column(unique=true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public static class DefaultNames {
		
		public static final String PAPER = "БУМАГА"; 
		public static final String SPRING = "ПРУЖИНА";
		
		private static String[] defaultNames = new String[] {
			PAPER,
			SPRING
		};
		
		public static String[] all() {
			return defaultNames;
		}
	}

	
	
}
