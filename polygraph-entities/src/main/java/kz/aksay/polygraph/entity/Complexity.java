package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="complexity", 
	uniqueConstraints=@UniqueConstraint(
			columnNames={"name"}, name="unq_complexity_name"))
public class Complexity extends EntitySupport {
	
	public static final Complexity DIFFICULT = new Complexity();
	public static final Complexity MEDIUM = new Complexity();
	public static final Complexity EASY = new Complexity();
	
	static {
		DIFFICULT.setName("Сложно");
		DIFFICULT.setMin(8);
		DIFFICULT.setMax(10);
		
		MEDIUM.setName("Средне");
		MEDIUM.setMin(7);
		MEDIUM.setMax(4);
		
		EASY.setName("Легко");
		EASY.setMin(3);
		EASY.setMax(1);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 124709657521L;
	
	@NotNull
	@Column
	private String name;
	
	@Min(value=1)
	@Max(value=10)
	@NotNull
	@Column(name="min_value")
	private Integer min;
	
	@Min(value=1)
	@Max(value=10)
	@NotNull
	@Column(name="max_value")
	private Integer max;
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Complexity == false) return false;
		Complexity other = (Complexity)obj;
		if(id == null) {
			if(other.getId() != null) return false;
			return true;
		}
		if(other.getId() == null) return false;
		return id.equals(other.getId());
	}
	
	@Override
	public int hashCode() {
		if(id != null) {
			return id.hashCode();
		} else if(name != null) {
			return name.hashCode();
		}
		return -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}
	
	
}
