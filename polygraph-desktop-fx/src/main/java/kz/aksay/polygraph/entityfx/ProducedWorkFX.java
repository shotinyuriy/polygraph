package kz.aksay.polygraph.entityfx;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.fxapi.MaterialConsumptionHolderFX;
import kz.aksay.polygraph.util.FormatUtil;

public class ProducedWorkFX extends EntityFX<ProducedWork> implements MaterialConsumptionHolderFX {
	
	
	private EmployeeFX executorFX;
	private WorkTypeFX workTypeFX;
	private EquipmentFX equipmentFX;
	private ObservableList<MaterialConsumptionFX> materialConsumptionProperty;
	private boolean dirty = false;
	
	public ProducedWorkFX(ProducedWork producedWork) {
		
		super(producedWork);
		if(producedWork.getExecutor() != null) {
			executorFX = new EmployeeFX(producedWork.getExecutor());
		}
		if(producedWork.getWorkType() != null) {
			workTypeFX = new WorkTypeFX(producedWork.getWorkType());
		}
		if(producedWork.getEquipment() != null) {
			equipmentFX = new EquipmentFX(producedWork.getEquipment());
		}
		
		List<MaterialConsumptionFX> materialConsumptionsFX 
			= EntityFX.convertListEntityToFX(
				producedWork.getMaterialConsumption(), MaterialConsumptionFX.class);
		materialConsumptionProperty = FXCollections.observableArrayList(
			materialConsumptionsFX);
	}
	
	@Override
	public ProducedWork getEntity() {
		fillEntity();
		return entity;
	}
	
	private void fillEntity() {
		entity.setMaterialConsumption(new HashSet<MaterialConsumption>());
		for(MaterialConsumptionFX materialConsumptionFX : materialConsumptionProperty) {
			MaterialConsumption materialConsumption = materialConsumptionFX.getEntity();
			materialConsumption.setProducedWork(entity);
			entity.getMaterialConsumption().add(materialConsumption);
		}
	}
	
	public EmployeeFX getExecutorFX() {
		return executorFX;
	}
	
	public WorkTypeFX getWorkTypeFX() {
		return workTypeFX;
	}
	
	public BigDecimal getCost() {
		if(entity.getCost() != null) {
			return entity.getCost();
		}
		return BigDecimal.ZERO;
	}
	
	public Integer getQuantity() {
		return entity.getQuantity();
	}
	
	public Integer getWasted() {
		return entity.getWasted();
	} 
	
	public String getBeginDateTimeString() {
		if(entity.getCreatedAt() != null) {
			return FormatUtil.dateFormatter.format(entity.getCreatedAt());
		}
		return null;
	}
	
	public String getWorkTypeName() {
		if( entity.getWorkType() != null) {
			return entity.getWorkType().getName();
		}
		return null;
	}
	
	public String getDescription() {
		
		StringBuffer sb = new StringBuffer();
		
		if( entity.getWorkType() != null) {
			sb.append(entity.getWorkType().getName());
		}
		
		if(entity.getEquipment() != null) {
			sb.append(" ").append(entity.getEquipment().getName());
		}
		
		return sb.toString();
	}
	
	public String getExecutorName() {
		{
			return executorFX.toString();
		}
	}

	public boolean isDirty() {
		return this.entity.isDirty();
	}

	public void setDirty(boolean dirty) {
		this.entity.setDirty(dirty);
	}

	public void setMaterialConsumption(
			Set<MaterialConsumption> materialConsumption) {
		this.entity.setMaterialConsumption(materialConsumption);
	}

	public Long getId() {
		return entity.getId();
	}

	public EquipmentFX getEquipmentFX() {
		
		return equipmentFX;
	}
	
	public Integer getEquipmentOrderNumber() {
		return entity.getEquipmentOrderNumber();
	}

	@Override
	public ObservableList<MaterialConsumptionFX> getMaterialConsumptionFX() {
		return materialConsumptionProperty;
	}

	@Override
	public void setMaterialConsumptionFX(
			ObservableList<MaterialConsumptionFX> materialConsumption) {
		this.materialConsumptionProperty = materialConsumption;
		
	}

	@Override
	public Set<MaterialConsumption> getMaterialConsumption() {
		return entity.getMaterialConsumption();
	}

	@Override
	public boolean isAllowedToEdit() {
		return true;
	}

	public String getEquipmentOrderNumberString() {
		if(entity.getEquipmentOrderNumber() != null)
			return entity.getEquipmentOrderNumber().toString();
		return null;
	}
	
}