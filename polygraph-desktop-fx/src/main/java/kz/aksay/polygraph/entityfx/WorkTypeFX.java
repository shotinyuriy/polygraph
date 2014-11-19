package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.util.FormatUtil;

public class WorkTypeFX {

	private WorkType workType;
	
	public static Collection<WorkTypeFX> convertListEntityToFX(
			Collection<WorkType> workTypes) {
		Collection<WorkTypeFX> workTypesFX = new LinkedList<>();
		for(WorkType workType : workTypes) {
			workTypesFX.add(new WorkTypeFX(workType));
		}
		return workTypesFX;
	}
	
	public WorkTypeFX(WorkType workType) {
		this.workType = workType;
	}
	
	public String getCreatedAtString() {
		if(workType.getCreatedAt() != null) {
			return FormatUtil.dateFormatter.format(workType.getCreatedAt());
		}
		return null;
	}
	
	public String getCreatedByLogin() {
		if(workType.getCreatedBy() != null) {
			return workType.getCreatedBy().getLogin();
		}
		return null;
	}
	
	public String getName() {
		return workType.getName();
	}
	
	public String toString() {
		return workType.getName();
	}

	public WorkType getWorkType() {
		return workType;
	}
}
