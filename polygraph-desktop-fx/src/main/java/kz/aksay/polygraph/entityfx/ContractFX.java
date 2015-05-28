package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.util.FormatUtil;

public class ContractFX extends EntityFX<Contract> {

	public ContractFX(Contract entity) {
		super(entity);
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

	public String getPartyName1() {
		if(entity.getParty1() != null) {
			return entity.getParty1().getShortname();
		}
		return null;
	}
	
	public String getPartyName2() {
		if(entity.getParty2() != null) {
			return entity.getParty2().getShortname();
		}
		return null;
	}
}
