package kz.aksay.polygraph.entityfx;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.util.FormatUtil;

public class ProducedWorkFX {
	
	private ProducedWork producedWork;
	private EmployeeFX executorFX;
	private WorkTypeFX workTypeFX;
	private EquipmentFX equipmentFX;
	private boolean dirty = false;
	
	public static Collection<ProducedWorkFX> convertListEntityToFX(
			Collection<ProducedWork> producedWorks) {
		Collection<ProducedWorkFX> producedWorksFX = new LinkedList<>();
		if(producedWorks != null) {
			for(ProducedWork producedWork : producedWorks) {
				producedWorksFX.add(new ProducedWorkFX(producedWork));
			}
		}
		return producedWorksFX;
	}

	public ProducedWorkFX(ProducedWork producedWork) {
		this.producedWork = producedWork;
		if(producedWork.getExecutor() != null) {
			executorFX = new EmployeeFX(producedWork.getExecutor());
		}
		if(producedWork.getWorkType() != null) {
			workTypeFX = new WorkTypeFX(producedWork.getWorkType());
		}
		if(producedWork.getEquipment() != null) {
			equipmentFX = new EquipmentFX(producedWork.getEquipment());
		}
	}
	
	public ProducedWork getProducedWork() {
		return producedWork;
	}
	
	public EmployeeFX getExecutorFX() {
		return executorFX;
	}
	
	public WorkTypeFX getWorkTypeFX() {
		return workTypeFX;
	}
	
	public BigDecimal getCost() {
		if(producedWork.getCost() != null) {
			return producedWork.getCost();
		}
		return BigDecimal.ZERO;
	}
	
	public String getBeginDateTimeString() {
		if(producedWork.getCreatedAt() != null) {
			return FormatUtil.dateFormatter.format(producedWork.getCreatedAt());
		}
		return null;
	}
	
	public String getFinishDateTimeString() {
		if(producedWork.getFinishedAt() != null) {
			return FormatUtil.dateFormatter.format(producedWork.getFinishedAt());
		}
		return null;
	}
	
	public String getWorkTypeName() {
		if( producedWork.getWorkType() != null) {
			return producedWork.getWorkType().getName();
		}
		return null;
	}
	
	public String getDescription() {
		
		StringBuffer sb = new StringBuffer();
		
		if( producedWork.getWorkType() != null) {
			sb.append(producedWork.getWorkType().getName());
		}
		
		if(producedWork.getEquipment() != null) {
			sb.append(" ").append(producedWork.getEquipment().getName());
		}
		
		return sb.toString();
	}
	
	public String getExecutorName() {
		{
			return executorFX.toString();
		}
	}

	public boolean isDirty() {
		return this.producedWork.isDirty();
	}

	public void setDirty(boolean dirty) {
		this.producedWork.setDirty(dirty);
	}

	public void finishWork() {
		this.producedWork.setFinishedAt(new Date());
	}

	public void setMaterialConsumption(
			Set<MaterialConsumption> materialConsumption) {
		this.producedWork.setMaterialConsumption(materialConsumption);
	}

	public Long getId() {
		return producedWork.getId();
	}

	public EquipmentFX getEquipmentFX() {
		
		return equipmentFX;
	}
}