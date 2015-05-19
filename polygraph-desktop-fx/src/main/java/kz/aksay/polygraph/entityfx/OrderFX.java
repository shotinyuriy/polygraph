package kz.aksay.polygraph.entityfx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kz.aksay.polygraph.entity.Subject;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.fxapi.MaterialConsumptionHolderFX;
import kz.aksay.polygraph.util.FormatUtil;

public class OrderFX implements MaterialConsumptionHolderFX {
	
	private Order order;
	private EmployeeFX currentExecutorFX;
	
	private LongProperty idProperty;
	private ObjectProperty<Date> createdAtProperty;
	private ObjectProperty<User> createdByProperty;
	private ObjectProperty<Subject> customerProperty;
	private ObjectProperty<EmployeeFX> currentExecutorProperty;
	private ObjectProperty<Date> updatedAtProperty;
	private ObjectProperty<User> updatedByProperty;
	private ObjectProperty<Date> dateEndPlanProperty;
	private ObjectProperty<Order.State> stateProperty;
	private StringProperty descriptionProperty;
	private ObservableList<ProducedWorkFX> producedWorkProperty;
	private ObservableList<MaterialConsumptionFX> materialConsumptionProperty;
	
	public OrderFX(Order order) {
		if(order == null) {
			order = new Order();
		}
		this.order = order;
		if(order != null) {
			currentExecutorFX = new EmployeeFX(order.getCurrentExecutor());
			createdAtProperty = new SimpleObjectProperty<Date>(order.getCreatedAt());
			createdByProperty = new SimpleObjectProperty<User>(order.getCreatedBy());
			currentExecutorProperty = new SimpleObjectProperty<EmployeeFX>(currentExecutorFX);
			customerProperty = new SimpleObjectProperty<Subject>(order.getCustomer());
			updatedAtProperty = new SimpleObjectProperty<Date>(order.getUpdatedAt());
			updatedByProperty = new SimpleObjectProperty<User>(order.getUpdatedBy());
			setDateEndPlanProperty(new SimpleObjectProperty<Date>(order.getDateEndPlan()));
			stateProperty = new SimpleObjectProperty<Order.State>(order.getState());
			descriptionProperty = new SimpleStringProperty(order.getDescription());
			
			Collection<ProducedWorkFX> producedWorksFX = ProducedWorkFX.
					convertListEntityToFX(order.getProducedWorks());
			
			producedWorkProperty = FXCollections.observableArrayList(
					producedWorksFX);
			
			Collection<MaterialConsumptionFX> materialConsumptionsFX 
				= MaterialConsumptionFX.convertListEntityToFX(
						order.getMaterialConsumption());
			materialConsumptionProperty = FXCollections.observableArrayList(
					materialConsumptionsFX);
		}
	}
	
	public Order getOrder() {
		fillOrder();
		return order;
	}
	
	public static List<OrderFX> convertListEntityToFX(List<Order> orders) {
		 List<OrderFX> ordersFX = new ArrayList<>();
		 if(orders != null) {
			 for(Order order : orders) {
				 ordersFX.add(new OrderFX(order));
			 }
		 }
		 return ordersFX;
	}
	
	public String getCreatedAtString() {
		return FormatUtil.dateFormatter.format(order.getCreatedAt());
	}
	
	public String getUpdatedAtString() {
		if(order.getUpdatedAt() != null)
			return FormatUtil.dateFormatter.format(order.getUpdatedAt());
		return null;
	}
	
	public String getDateEndPlanString() {
		if(order.getDateEndPlan() != null)
			return FormatUtil.dateFormatter.format(order.getDateEndPlan());
		return null;
	}
	
	public String getCustomerFullName() {
		Subject customer = order.getCustomer();
		
		if( customer != null ) {
			return customer.getFullName();
		}
		
		return null;
	}
	
	public String getNumber() {
		return order.getId().toString();
	}
	
	public EmployeeFX getCurrentExecutorFX() {
		return currentExecutorFX;
	}
	
	public String getDescription() {
		return order.getDescription();
	}

	public Long getCustomerId() {
		Subject customer = order.getCustomer();
		if(customer != null) {
			return customer.getId();
		}
		return null;
	}
	
	public String getCurrentExecutorDescription() {
		return currentExecutorFX.toString();
	}
	
	public ObservableList<MaterialConsumptionFX> getMaterialConsumptionFX() {
		return materialConsumptionProperty;
	}
	
	public void setMaterialConsumptionFX(
			ObservableList<MaterialConsumptionFX> materialConsumption) {
		materialConsumptionProperty = materialConsumption;
	}
	
	public Set<MaterialConsumption> getMaterialConsumption() {
		Set<MaterialConsumption> resultSet = new HashSet<>();
		for(MaterialConsumptionFX materialConsumptionFX : materialConsumptionProperty) {
			resultSet.add(materialConsumptionFX.getMaterialConsumption());
		}
		return resultSet;
	}

	public void setMaterialConsumption(
			Set<MaterialConsumption> materialConsumptions) {
		materialConsumptionProperty.clear();
		for(MaterialConsumption materialConsumption : materialConsumptions) {
			materialConsumptionProperty.add(new MaterialConsumptionFX(materialConsumption));
		}
	}
	
	private void fillOrder() {
		order.setCreatedAt(createdAtProperty.get());
		order.setCreatedBy(createdByProperty.get());
		EmployeeFX currentExecutorFX = currentExecutorProperty.get();
		if(currentExecutorFX != null) {
			order.setCurrentExecutor(currentExecutorFX.getEmployee());
		}
		order.setCustomer(customerProperty.get());
		order.setDescription(descriptionProperty.get());
		
		order.setMaterialConsumption(new HashSet<MaterialConsumption>());
		for(MaterialConsumptionFX materialConsumptionFX : materialConsumptionProperty) {
			MaterialConsumption materialConsumption = materialConsumptionFX.getMaterialConsumption();
			System.out.println(materialConsumption);
			order.getMaterialConsumption().add(materialConsumption);
		}
		
		order.setProducedWorks(new HashSet<ProducedWork>());
		for(ProducedWorkFX producedWorkFX : producedWorkProperty) {
			ProducedWork producedWork = producedWorkFX.getProducedWork();
			producedWork.setOrder(order);
			order.getProducedWorks().add(producedWork);
		}
		
		order.setState(stateProperty.get());
	}

	

	public void setMaterialConsumptionProperty(
			SimpleListProperty<MaterialConsumptionFX> materialConsumptionProperty) {
		this.materialConsumptionProperty = materialConsumptionProperty;
	}

	public LongProperty getIdProperty() {
		return idProperty;
	}

	public void setIdProperty(LongProperty idProperty) {
		this.idProperty = idProperty;
	}

	public ObjectProperty<Date> getCreatedAtProperty() {
		return createdAtProperty;
	}

	public void setCreatedAtProperty(ObjectProperty<Date> createdAtProperty) {
		this.createdAtProperty = createdAtProperty;
	}

	public ObjectProperty<User> getCreatedByProperty() {
		return createdByProperty;
	}

	public void setCreatedByProperty(ObjectProperty<User> createdByProperty) {
		this.createdByProperty = createdByProperty;
	}

	public ObjectProperty<Subject> getCustomerProperty() {
		return customerProperty;
	}

	public void setCustomerProperty(ObjectProperty<Subject> customerProperty) {
		this.customerProperty = customerProperty;
	}

	public ObjectProperty<EmployeeFX> getCurrentExecutorProperty() {
		return currentExecutorProperty;
	}

	public void setCurrentExecutorProperty(
			ObjectProperty<EmployeeFX> currentExecutorProperty) {
		this.currentExecutorProperty = currentExecutorProperty;
	}

	public ObjectProperty<Date> getUpdatedAtProperty() {
		return updatedAtProperty;
	}

	public void setUpdatedAtProperty(ObjectProperty<Date> updatedAtProperty) {
		this.updatedAtProperty = updatedAtProperty;
	}

	public ObjectProperty<User> getUpdatedByProperty() {
		return updatedByProperty;
	}

	public void setUpdatedByProperty(ObjectProperty<User> updatedByProperty) {
		this.updatedByProperty = updatedByProperty;
	}

	public ObjectProperty<Order.State> getStateProperty() {
		return stateProperty;
	}

	public void setStateProperty(ObjectProperty<Order.State> stateProperty) {
		this.stateProperty = stateProperty;
	}

	public StringProperty getDescriptionProperty() {
		return descriptionProperty;
	}

	public void setDescriptionProperty(StringProperty descriptionProperty) {
		this.descriptionProperty = descriptionProperty;
	}

	public ObservableList<ProducedWorkFX> getProducedWorkProperty() {
		return producedWorkProperty;
	}

	public void setProducedWorkProperty(
			ObservableList<ProducedWorkFX> producedWorkProperty) {
		this.producedWorkProperty = producedWorkProperty;
	}

	public ObservableList<MaterialConsumptionFX> getMaterialConsumptionProperty() {
		return materialConsumptionProperty;
	}

	public void setMaterialConsumptionProperty(
			ObservableList<MaterialConsumptionFX> materialConsumptionProperty) {
		this.materialConsumptionProperty = materialConsumptionProperty;
	}

	public ObjectProperty<Date> getDateEndPlanProperty() {
		return dateEndPlanProperty;
	}

	public void setDateEndPlanProperty(ObjectProperty<Date> dateEndPlanProperty) {
		this.dateEndPlanProperty = dateEndPlanProperty;
	}
}
