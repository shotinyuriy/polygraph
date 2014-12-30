package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Check;

@Entity
@Table(name="material_type", uniqueConstraints = {
		@UniqueConstraint(name="UNQ_MT_NAME", columnNames="name")})
@Check(constraints="name=UPPER(name)")
public class MaterialType extends EntitySupport {
	

	/**
	 * 
	 */
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
		
		public static final String PAPER = "БУМАГА"; 
		public static final String BINDING_SPRING = "ПРУЖИНА";
		public static final String PAPER_CLIP = "СКРЕПКА";
		
		private static String[] defaultNames = new String[] {
			PAPER,
			BINDING_SPRING,
			PAPER_CLIP
		};
		
		public static String[] all() {
			return defaultNames;
		}
	}

	
	
}
