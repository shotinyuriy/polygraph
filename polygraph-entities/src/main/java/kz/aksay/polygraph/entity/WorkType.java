package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Check;

@Entity
@Table(name="work_type")
@Check(constraints="name=UPPER(name)")
public class WorkType extends EntitySupport {
	
	@Column(unique=true, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static class DefaultNames {
		
		public static final String DEVELOPMENT = "РАЗРАБОТКА";
		public static final String CORRECTION = "КОРРЕКТИРОВКА";
		public static final String SCANNING = "СКАНИРОВАНИЕ";
		public static final String BINDING = "ПЕРЕПЛЕТ";
		public static final String CUTTING = "РЕЗКА";
		public static final String LAMINATION = "ЛАМИНИРОВАНИЕ";
		public static final String COPY = "КОПИРОВАНИЕ";
		public static final String PRINTING = "ПЕЧАТЬ";
		
		private static String[] defaultNames = new String[] {
			DEVELOPMENT,
			CORRECTION,
			SCANNING,
			BINDING,
			CUTTING,
			LAMINATION,
			COPY,
			PRINTING
		};
		
		public static String[] all() {
			return defaultNames;
		}
	}
}
