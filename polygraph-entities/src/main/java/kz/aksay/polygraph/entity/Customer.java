package kz.aksay.polygraph.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="customer")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Customer extends EntitySupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1838755615813471835L;
	
	public abstract String getFullName();
	
}
