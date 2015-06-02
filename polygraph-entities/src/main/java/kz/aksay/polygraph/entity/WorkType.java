package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="work_type", uniqueConstraints={
			@UniqueConstraint(name="UNQ_WT_CODE", columnNames={"code"}), 
			@UniqueConstraint(name="UNQ_WT_NAME",columnNames={"name"})
		}
)
@Check(constraints="name=UPPER(name) AND code=UPPER(code)")
public class WorkType extends EntitySupport {
	
	public static final WorkType DEVELOPMENT = new WorkType();
	public static final WorkType CORRECTION = new WorkType();
	public static final WorkType SCANNING = new WorkType();
	public static final WorkType BINDING = new WorkType();
	public static final WorkType CUTTING = new WorkType();
	public static final WorkType LAMINATION = new WorkType();
	public static final WorkType COPYING = new WorkType();
	public static final WorkType PRINTING_BLACK_AND_WHITE = new WorkType();
	public static final WorkType PRINTING_COLORED = new WorkType();
	
	public static final WorkType[] DEFAULTS = new WorkType[] {
		DEVELOPMENT,
		CORRECTION,
		SCANNING,
		BINDING,
		CUTTING,
		LAMINATION,
		COPYING,
		PRINTING_BLACK_AND_WHITE,
		PRINTING_COLORED
	};
	
	@NotNull
	@NotEmpty
	@Column
	private String name;
	
	@NotNull
	@NotEmpty
	@Pattern(regexp="[A-Z0-9_].*")
	@Column
	private String code;
	
	@Column
	private String code1c;
	
	static {
		DEVELOPMENT.name = "РАЗРАБОТКА";
		DEVELOPMENT.code = "DEVELOPMENT";
		DEVELOPMENT.code1c = "001001";
		
		CORRECTION.name = "КОРРЕКТИРОВКА";
		CORRECTION.code = "CORRECTION";
		CORRECTION.code1c = "001002";
		
		SCANNING.name = "СКАНИРОВАНИЕ";
		SCANNING.code = "SCANNING";
		SCANNING.code1c = "001003";
		
		BINDING.name = "ПЕРЕПЛЕТ";
		BINDING.code = "BINDING";
		BINDING.code1c = "001004";
		
		CUTTING.name = "РЕЗКА";
		CUTTING.code = "CUTTING";
		CUTTING.code1c = "001005";
		
		LAMINATION.name = "ЛАМИНИРОВАНИЕ";
		LAMINATION.code = "LAMINATION";
		LAMINATION.code1c = "001006";
		
		COPYING.name = "КОПИРОВАНИЕ";
		COPYING.code = "COPYING";
		COPYING.code1c = "001007";
		
		PRINTING_BLACK_AND_WHITE.name = "Ч/Б ПЕЧАТЬ";
		PRINTING_BLACK_AND_WHITE.code = "PRINTING_BLACK_AND_WHITE";
		PRINTING_BLACK_AND_WHITE.code1c = "001008";
		
		PRINTING_COLORED.name = "ЦВЕТНАЯ ПЕЧАТЬ";
		PRINTING_COLORED.code = "PRINTING_COLORED";
		PRINTING_COLORED.code1c = "001009";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return this.code.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof WorkType)) return false;
		WorkType other = (WorkType) obj;
		if(this.code == null) {
			if(other.getCode() != null) return false;
			return true;
		} else {
			if(other.code == null) return false;
			return this.code.equals(other.code);
		}
	}

	public String getCode1c() {
		return code1c;
	}

	public void setCode1c(String code1c) {
		this.code1c = code1c;
	}
}
