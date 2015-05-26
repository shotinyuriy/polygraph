package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.MaterialFX;
import kz.aksay.polygraph.entityfx.MaterialTypeFX;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.service.PaperTypeService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MaterialTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<MaterialFX> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<MaterialFX, String> nameColumn;
	@FXML	private TableColumn<MaterialFX, PaperType> materialTypeColumn;
	@FXML	private ComboBox<MaterialTypeFX> materialTypeBox;
	
	private Map<String, Object> session;
	
	private IPaperTypeService paperTypeService 
		= ContextUtils.getBean(IPaperTypeService.class);
	
	private IMaterialService materialService 
	= ContextUtils.getBean(IMaterialService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		ObservableList<MaterialFX> data = tableView.getItems();
		MaterialTypeFX materialTypeFx = materialTypeBox.getValue();
		PaperType paperType = materialTypeFx.getMaterialType();
		Material material = createMaterial(nameField.getText(), paperType);
		save(material);
		MaterialFX materialFx = new MaterialFX(material);
		data.add(materialFx);
		nameField.setText("");
		materialTypeBox.setValue(null);
	}
	
	@FXML
	protected void delete(ActionEvent event) {
		MaterialFX materialFX = tableView.getSelectionModel().getSelectedItem();
		if(materialFX != null) {
			Material material = materialFX.getMaterial();
			try {
				materialService.delete(material);
				tableView.getItems().remove(materialFX);
			}
			catch(ValidationException ve) {
				validationLabel.setText(ve.getMessage());
			}
		}
	}
	
	@FXML
	protected void updateName(
			TableColumn.CellEditEvent<MaterialFX, String> cellEditEvent) {
		MaterialFX materialFx = cellEditEvent.getRowValue();
		Material material = materialFx.getMaterial();
		material.setName(cellEditEvent.getNewValue());
		material.setUpdatedAt(new Date());
		material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(material);
	}
	
	@FXML
	protected void updateMaterialType(
			TableColumn.CellEditEvent<MaterialFX, String> cellEditEvent) {
		MaterialFX materialFx = cellEditEvent.getRowValue();
		Material material = materialFx.getMaterial();
		material.setName(cellEditEvent.getNewValue());
		material.setUpdatedAt(new Date());
		material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(material);
	}
	
	protected void save(Material material) {
		try {
			materialService.save(material);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Material createMaterial(String name, PaperType paperType) {
		Material mt = new Material();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setName(name.toString());
		mt.setMaterialType(paperType);
		return mt;
	}

	public TableView<MaterialFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<MaterialFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		List<MaterialFX> materialsFx 
			= MaterialFX.convertListEntityToFX(materialService.findAll());
		tableView.getItems().setAll(materialsFx);
		nameColumn.setCellFactory(
				TextFieldTableCell.<MaterialFX>forTableColumn());
		
		List<MaterialTypeFX> materialTypesFX 
			= MaterialTypeFX.convertListEntityToFX(paperTypeService.findAll());
		materialTypeBox.getItems().setAll(materialTypesFX);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
