package kz.aksay.polygraph.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import kz.aksay.polygraph.service.DefaultDataCreationService;
import kz.aksay.polygraph.validation.Comparison;
import kz.aksay.polygraph.validation.DateEquation;
import kz.aksay.polygraph.validation.DateEquations;

@Entity
@Table(name="\"order\"")
@DateEquations(dateEquations={
	@DateEquation(comparison=Comparison.GREATER_THAN_OR_EQUALS, date1Name="dateEndPlan", date2Name="createdAt", 
			message="Плановая дата окончания не может быть меньше даты создания заказа"),
	@DateEquation(comparison=Comparison.GREATER_THAN_OR_EQUALS, date1Name="dateEndReal", date2Name="createdAt",
			message="Реальная дата окончания не может быть меньше даты создания заказа")
})
public class Order extends OrderRootSupport implements MaterialConsumer {
	
	private static final long serialVersionUID = -6315648934763901488L;
	
	public static enum State {
		NEW("Новый"),
		PROCESSING("В работе"),
		PAUSED("Приостановлен"),
		READY("Готов"),
		FINISHED("Завершен"),
		CANCELLED("Отклонен");
		
		private String name;
		
		private State(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	@Transient
	private Set<MaterialConsumption> materialConsumption;
	
	@Column
	private String code1c;
	
	@NotNull(message="Не указан заказчик в заказе")
	@ManyToOne
	@JoinColumn(name="customer_id", nullable=false)
	private Subject customer;
	
	@Column
	private Integer circulation;
	
	@ManyToOne
	@JoinColumn(name="vicarious_power_id")
	private VicariousPower vicariousPower;
	
	@ManyToOne
	@JoinColumn(name="current_executor_id")
	private Employee currentExecutor;
	
	@NotNull(message="Не указана плановая дата завершения")
	@Column(name="date_end_plan")
	private Date dateEndPlan;
	
	@Column(name="date_end_real")
	private Date dateEndReal;
	
	@Transient
	private Set<ProducedWork> producedWorks;

	@Column
	private String description;
	
	@Column
	private String projectPath;
	
	@Column
	@Enumerated(EnumType.STRING)
	private State state;
	
	@ManyToOne
	@JoinColumn(name="cmplex")
	@NotNull(message="Не указана сложность")
	private Complexity complexity = Complexity.MEDIUM;

	@Transient
	public BigDecimal getTotalCost() {
		BigDecimal totalCost = BigDecimal.ZERO;
		if(producedWorks != null) {
			for(ProducedWork producedWork : producedWorks) {
				BigDecimal cost;
				if((cost = producedWork.getCost()) != null)
					totalCost = totalCost.add(cost);
				
				if(producedWork.getMaterialConsumption() != null) {
					for(MaterialConsumption currentMatCon : producedWork.getMaterialConsumption()) {
						BigDecimal costMC;
						if((costMC = currentMatCon.getCost()) != null)
							totalCost = totalCost.add(costMC);
					}
				}
			}
		}
		
		
		return totalCost;
	}
	
	public Subject getCustomer() {
		return customer;
	}

	public void setCustomer(Subject customer) {
		this.customer = customer;
	}
	
	public Employee getCurrentExecutor() {
		return currentExecutor;
	}

	public void setCurrentExecutor(Employee currentExecutor) {
		this.currentExecutor = currentExecutor;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ProducedWork> getProducedWorks() {
		return producedWorks;
	}

	public void setProducedWorks(Set<ProducedWork> producedWorks) {
		this.producedWorks = producedWorks;
	}

	public Set<MaterialConsumption> getMaterialConsumption() {
		return materialConsumption;
	}

	public void setMaterialConsumption(Set<MaterialConsumption> materialConsumption) {
		this.materialConsumption = materialConsumption;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Order [");
		sb.append("id: ").append(getId());
		sb.append("]");
		
		return sb.toString();
	}

	public Date getDateEndPlan() {
		return dateEndPlan;
	}

	public void setDateEndPlan(Date dateEndPlan) {
		this.dateEndPlan = dateEndPlan;
	}

	public VicariousPower getVicariousPower() {
		return vicariousPower;
	}

	public void setVicariousPower(VicariousPower vicariousPower) {
		this.vicariousPower = vicariousPower;
	}

	public Date getDateEndReal() {
		return dateEndReal;
	}

	public void setDateEndReal(Date dateEndReal) {
		this.dateEndReal = dateEndReal;
	}

	public Integer getCirculation() {
		return circulation;
	}

	public void setCirculation(Integer circulation) {
		this.circulation = circulation;
	}

	public Complexity getComplexity() {
		return complexity;
	}

	public void setComplexity(Complexity complexity) {
		this.complexity = complexity;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
}
