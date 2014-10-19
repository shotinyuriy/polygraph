package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.MaterialTypeService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MaterialTypeTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<MaterialType> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<MaterialType, String> nameColumn;
	
	private Map<String, Object> session;
	
	private MaterialTypeService materialTypeService 
		= ContextUtils.getBean(MaterialTypeService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		ObservableList<MaterialType> data = tableView.getItems();
		MaterialType newMaterialType = createMaterialType(nameField.getText()); 
		data.add(newMaterialType);
		save(newMaterialType);
		nameField.setText("");
	}
	
	@FXML
	protected void update(
			TableColumn.CellEditEvent<MaterialType, String> cellEditEvent) {
		MaterialType materialType = cellEditEvent.getRowValue();
		materialType.setName(cellEditEvent.getNewValue());
		materialType.setUpdatedAt(new Date());
		materialType.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(materialType);
	}
	
	protected void save(MaterialType materialType) {
		try {
			materialTypeService.save(materialType);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected MaterialType createMaterialType(String name) {
		MaterialType mt = new MaterialType();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setName(name.toString());
		return mt;
	}

	public TableView<MaterialType> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<MaterialType> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {		
		tableView.getItems().setAll(materialTypeService.findAll());
		nameColumn.setCellFactory(
				TextFieldTableCell.<MaterialType>forTableColumn());
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
