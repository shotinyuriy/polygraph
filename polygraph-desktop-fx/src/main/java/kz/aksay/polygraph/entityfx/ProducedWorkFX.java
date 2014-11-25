package kz.aksay.polygraph.entityfx;

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
	private boolean dirty = false;
	
	public static Collection<ProducedWorkFX> contvertListEntityToFX(
			Collection<ProducedWork> producedWorks) {
		Collection<ProducedWorkFX> producedWorksFX = new LinkedList<>();
		for(ProducedWork producedWork : producedWorks) {
			producedWorksFX.add(new ProducedWorkFX(producedWork));
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
	
	public String getBeginDateTimeString() {
		if(producedWork.getCreatedAt() != null) {
			return FormatUtil.dateTimeFormatter.format(producedWork.getCreatedAt());
		}
		return null;
	}
	
	public String getFinishDateTimeString() {
		if(producedWork.getFinishedAt() != null) {
			return FormatUtil.dateTimeFormatter.format(producedWork.getFinishedAt());
		}
		return null;
	}
	
	public String getWorkTypeName() {
		if( producedWork.getWorkType() != null) {
			return producedWork.getWorkType().getName();
		}
		return null;
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
}