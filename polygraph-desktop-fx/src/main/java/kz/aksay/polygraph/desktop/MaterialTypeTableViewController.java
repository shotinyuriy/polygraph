package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.desktop.model.Person;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.MaterialTypeService;
import kz.aksay.polygraph.util.ContextUtils;

public class MaterialTypeTableViewController implements Initializable {
	@FXML	private TableView<MaterialType> tableView;
	@FXML	private TextField nameField;
	
	private MaterialTypeService materialTypeService 
		= ContextUtils.getBean(MaterialTypeService.class);
	
	@FXML
	protected void addMaterialType(ActionEvent event) {
		ObservableList<MaterialType> data = tableView.getItems();
		data.add(createMaterialType(nameField.getText()));
		
		nameField.setText("");
		
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
	}
}
