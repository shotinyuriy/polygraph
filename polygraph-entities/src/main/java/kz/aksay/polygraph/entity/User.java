package kz.aksay.polygraph.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="\"user\"")
public class User implements Serializable {
	
	public static enum Role {
		ADMIN,
		DIRECTOR,
		DESIGNER
	}
	
	public static User TECH_USER;
	
	static {
		TECH_USER = new User();
		TECH_USER.setCreatedAt(new Date());
		TECH_USER.setCreatedBy(null);
		TECH_USER.setLogin("techUser");
		TECH_USER.setPassword("test");
		TECH_USER.setRole(Role.ADMIN);
	}
	
	@Id
	@SequenceGenerator(name="userPKgenerator", initialValue=1, allocationSize=1, 
		sequenceName="user_sequence")
	@GeneratedValue(generator="userPKgenerator")
	protected Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="created_by")
	protected User createdBy;
	
	@Column(name="created_at", nullable=false)
	protected Date createdAt;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="updated_by")
	protected User updatedBy;
	
	@Column(name="updated_at")
	protected Date updatedAt;
	
	@Column(name="login", nullable=false, unique=true)
	private String login;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable=false)
	private Role role;
	
	@OneToOne
	@JoinColumn(name="employee_id", unique=true)
	private Employee employee;
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
