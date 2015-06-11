package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.util.FormatUtil;

public class WorkTypeFX extends EntityFX<WorkType> {
	
	public WorkTypeFX(WorkType entity) {
		super(entity);
	}

	public static Collection<WorkTypeFX> convertListEntityToFX(
			Collection<WorkType> workTypes) {
		Collection<WorkTypeFX> workTypesFX = new LinkedList<>();
		for(WorkType workType : workTypes) {
			workTypesFX.add(new WorkTypeFX(workType));
		}
		return workTypesFX;
	}
	
	public String getName() {
		return entity.getName();
	}
	
	public String toString() {
		return entity.getName();
	}

	public WorkType getWorkType() {
		return entity;
	}
}
