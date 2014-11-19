package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.util.FormatUtil;

public class ProducedWorkFX {
	
	private ProducedWork producedWork;
	private EmployeeFX executorFX;
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
	}
	
	public ProducedWork getProducedWork() {
		return producedWork;
	}
	
	public String getBeginDateTimeString() {
		if(producedWork.getCreatedAt() != null) {
			return FormatUtil.dateTimeFormatter.format(producedWork.getCreatedAt());
		}
		return null;
	}
	
	public String getEndDateTimeString() {
		if(producedWork.getUpdatedAt() != null) {
			return FormatUtil.dateTimeFormatter.format(producedWork.getUpdatedAt());
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
		EmployeeFX executorFX = new EmployeeFX(producedWork.getExecutor());
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
}