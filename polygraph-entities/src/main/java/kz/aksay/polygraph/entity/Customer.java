package kz.aksay.polygraph.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="customer")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1838755615813471835L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	protected Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by", nullable = false)
	protected User createdBy;

	@Column(name = "created_at", nullable = false)
	protected Date createdAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "updated_by")
	protected User updatedBy;

	@Column(name = "updated_at")
	protected Date updatedAt;

	public abstract String getFullName();
	
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
	
}
