package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="equipment")
public class Equipment extends EntitySupport {
	
	private static final long serialVersionUID = -7089946524123373267L;
	
	@Column(unique=true)
	private String name;
	
	@Column(name="monochrome_usage_count")
	private int monochromeUsageCount = 0;
	
	@Column(name="colored_usage_count")
	private int coloredUsageCount = 0;
	
	@Column(name="orders_count")
	private Integer ordersCount = 0;
	
	@Override
	public int hashCode() {
		if(id != null) {
			return id.hashCode();
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null) return false;
		if(object instanceof Equipment == false) return false;
		Equipment other = (Equipment) object;
		if(this.id == null) {
			if(other.getId() != null) return false;
			return true;
		}
		if(other.getId() == null) return true;
		return this.id.equals(other.getId());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMonochromeUsageCount() {
		return monochromeUsageCount;
	}

	public void setMonochromeUsageCount(int monochromeUsageCount) {
		this.monochromeUsageCount = monochromeUsageCount;
	}

	public int getColoredUsageCount() {
		return coloredUsageCount;
	}

	public void setColoredUsageCount(int coloredUsageCount) {
		this.coloredUsageCount = coloredUsageCount;
	}

	public Integer getOrdersCount() {
		return ordersCount;
	}

	public void setOrdersCount(Integer ordersCount) {
		this.ordersCount = ordersCount;
	}
	
	
}
