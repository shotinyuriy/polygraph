package kz.aksay.polygraph.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="order_full_text_index")
public class OrderFullTextIndex {

	@Id
	@TableGenerator(name="order_full_text_index_sequence", table="order_full_text_index_sequence")
	@GeneratedValue(strategy=GenerationType.TABLE, generator="order_full_text_index_sequence")
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="full_text_index_id", nullable=false)
	private FullTextIndex fullTextIndex;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="order_id", nullable=false)
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FullTextIndex getFullTextIndex() {
		return fullTextIndex;
	}

	public void setFullTextIndex(FullTextIndex fullTextIndex) {
		this.fullTextIndex = fullTextIndex;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}