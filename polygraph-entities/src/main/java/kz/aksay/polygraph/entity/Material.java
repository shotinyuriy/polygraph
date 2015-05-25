package kz.aksay.polygraph.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material")
@Inheritance(strategy=InheritanceType.JOINED)
public class Material extends EntitySupport {
	
	protected String materialTypeName;
	
	private static final long serialVersionUID = -7597841704963862971L;

//	@ManyToOne
//	@JoinColumn(name="material_type_id")
//	private MaterialType materialType;

	@Column
	private String name;

//	public MaterialType getMaterialType() {
//		return materialType;
//	}
//
//	public void setMaterialType(MaterialType materialType) {
//		this.materialType = materialType;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Material [");
		sb.append("id: ").append(id).append(", ");
		sb.append("name: ").append(" ").append(name).append(", ");
		sb.append("]");
		
		return sb.toString();
	}
	
	public static class DefaultNames {
		public static final Map<String, String[]> materialNames;
		
		static {
			materialNames = new HashMap<String, String[]>();
			materialNames.put(MaterialType.DefaultNames.PAPER, 
					new String[] {
					"A4 Gloss 250", "A4 Col+", "A3 Gloss 250"} );
		}
	}
}
