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
	
	@ManyToOne
	@JoinColumn(name="work_type_id")
	private WorkType workType;
	
	@Column
	private int monochromeUsageCount = 0;
	
	@Column
	private int coloredUsageCount = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WorkType getWorkType() {
		return workType;
	}

	public void setWorkType(WorkType workType) {
		this.workType = workType;
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
	
	
}
