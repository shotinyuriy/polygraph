package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Check;

@Entity
@Table(name="full_text_index", 
	uniqueConstraints=@UniqueConstraint(columnNames={"text"}, name="full_text_unique"))
public class FullTextIndex {

	@Id
	@TableGenerator(name="full_text_index_sequence", table="full_text_index_sequence")
	@GeneratedValue(strategy=GenerationType.TABLE, generator="full_text_index_sequence")
	private Long id;
	
	@Column
	@Check(constraints="text=LOWER(text)")
	private String text;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
