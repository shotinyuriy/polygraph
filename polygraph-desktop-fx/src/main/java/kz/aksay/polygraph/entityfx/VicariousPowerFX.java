package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.VicariousPower;
import kz.aksay.polygraph.util.FormatUtil;

public class VicariousPowerFX extends EntityFX<VicariousPower> {

	public VicariousPowerFX(VicariousPower entity) {
		super(entity);
	}

	public String getPersonName() {
		if(entity.getPerson() != null) {
			return entity.getPerson().getFullName();
		}
		return entity.getPersonName();
	}
	
	public String getNumber() {
		return entity.getNumber();
	}
	
	public String getBeginDateString() {
		if(entity.getBeginDate() != null) {
			return FormatUtil.dateFormatter.format(entity.getBeginDate());
		}
		return null;
	}
	
	public String getEndDateString() {
		if(entity.getEndDate() != null) {
			return FormatUtil.dateFormatter.format(entity.getEndDate());
		}
		return null;
	}

	public String getOrganizationName() {
		if(entity.getOrganization() != null) {
			return entity.getOrganization().getShortname();
		}
		return null;
	}
	
	public String getDescription() {
		if( entity != null) {
			StringBuffer description = new StringBuffer();
			description.append("Доверенность №").append(entity.getNumber());
			if(entity.getBeginDate() != null) {
				description.append(" от ").append(getBeginDateString());
			}
			description.append(" ").append(this.getPersonName());
			
			return description.toString();
		}
		return null;
	}
}
