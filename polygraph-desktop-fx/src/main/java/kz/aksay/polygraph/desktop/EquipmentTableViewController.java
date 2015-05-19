package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

import javax.validation.ValidationException;

import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EquipmentFX;
import kz.aksay.polygraph.entityfx.WorkTypeFX;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class EquipmentTableViewController implements Initializable, SessionAware {
	
	@FXML	private TableView<EquipmentFX> tableView;
	@FXML   private ComboBox<WorkTypeFX> workTypeBox;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<EquipmentFX, String> nameColumn;
	@FXML	private TableColumn<EquipmentFX, Integer> monochromeUsageCountColumn;
	@FXML	private TableColumn<EquipmentFX, Integer> coloredUsageCountColumn;
	
	private Map<String, Object> session;
	
	private IEquipmentService equipmentService 
		= ContextUtils.getBean(IEquipmentService.class);
	private IWorkTypeService workTypeService
		= ContextUtils.getBean(IWorkTypeService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		ObservableList<EquipmentFX> data = tableView.getItems();
		Equipment newEquipmentType = createEquipmentType(nameField.getText());
		EquipmentFX EquipmentFX = new EquipmentFX(newEquipmentType);
		data.add(EquipmentFX);
		save(newEquipmentType);
		nameField.setText("");
	}
	
	@FXML
	protected void update(
			TableColumn.CellEditEvent<EquipmentFX, String> cellEditEvent) {
		EquipmentFX EquipmentFX = cellEditEvent.getRowValue();
		Equipment Equipment = EquipmentFX.getEquipmentType();
		Equipment.setName(cellEditEvent.getNewValue());
		Equipment.setUpdatedAt(new Date());
		Equipment.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(Equipment);
	}
	
	protected void save(Equipment Equipment) {
		try {
			equipmentService.save(Equipment);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Equipment createEquipmentType(String name) {
		
		Equipment mt = new Equipment();
		WorkTypeFX workTypeFX = workTypeBox.getSelectionModel().getSelectedItem();
		
		if(workTypeFX != null) {
			mt.setWorkType(workTypeFX.getWorkType());
		}
		
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setName(name.toString());
		return mt;
	}

	public TableView<EquipmentFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<EquipmentFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		Collection<EquipmentFX> equipmentTypesFX 
			= EquipmentFX.convertListEntityToFX(equipmentService.findAll());
		tableView.getItems().setAll(equipmentTypesFX);
		nameColumn.setCellFactory(
				TextFieldTableCell.<EquipmentFX>forTableColumn());
		Collection<WorkTypeFX> workTypesFX
			= WorkTypeFX.convertListEntityToFX(workTypeService.findAll());
		workTypeBox.getItems().addAll(workTypesFX);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	
}
