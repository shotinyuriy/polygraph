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
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entityfx.EquipmentFX;
import kz.aksay.polygraph.entityfx.WorkTypeFX;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class EquipmentTableViewController implements Initializable, SessionAware {
	
	@FXML	private TableView<EquipmentFX> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<EquipmentFX, String> nameColumn;
	@FXML	private TableColumn<EquipmentFX, String> ordersCountColumn;
	
	private Map<String, Object> session;
	
	private IEquipmentService equipmentService 
		= ContextUtils.getBean(IEquipmentService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		ObservableList<EquipmentFX> data = tableView.getItems();
		Equipment newEquipmentType = createEquipmentType(nameField.getText());
		EquipmentFX EquipmentFX = new EquipmentFX(newEquipmentType);
		data.add(EquipmentFX);
		save(newEquipmentType);
		nameField.setText(null);
	}
	
	@FXML
	protected void updateName(
			TableColumn.CellEditEvent<EquipmentFX, String> cellEditEvent) {
		EquipmentFX EquipmentFX = cellEditEvent.getRowValue();
		Equipment Equipment = EquipmentFX.getEquipmentType();
		Equipment.setName(cellEditEvent.getNewValue());
		Equipment.setUpdatedAt(new Date());
		Equipment.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(Equipment);
	}
	
	@FXML
	protected void updateOrdersCount(
			TableColumn.CellEditEvent<EquipmentFX, String> cellEditEvent) {
		try {
			EquipmentFX equipmentFX = cellEditEvent.getRowValue();
			Equipment equipment = equipmentFX.getEquipmentType();
			
			equipment.setOrdersCount(Integer.parseInt(cellEditEvent.getNewValue()));
			equipment.setUpdatedAt(new Date());
			equipment.setUpdatedBy(SessionUtil.retrieveUser(session));
			
			save(equipment);
		} catch(NumberFormatException e) {
		}
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
		Collection<EquipmentFX> equipmentsFX 
			= EquipmentFX.convertListEntityToFX(equipmentService.findAll());
		
		loadCountsOfUsages(equipmentsFX);
		
		tableView.getItems().setAll(equipmentsFX);
		nameColumn.setCellFactory(
				TextFieldTableCell.<EquipmentFX>forTableColumn());
		
		ordersCountColumn.setCellFactory(TextFieldTableCell.<EquipmentFX>forTableColumn());
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	private void loadCountsOfUsages(final Collection<EquipmentFX> equipmentsFX) {
		for(final EquipmentFX equipmentFX : equipmentsFX) {
			ProducedWork producedWork = new ProducedWork();
			producedWork.setEquipment(equipmentFX.getEntity());
			
			producedWork.setFormat(Format.A3);
			producedWork.setWorkType(WorkType.PRINTING_COLORED);
			Integer coloredUsagesA3 = equipmentService.countOfUsagesByExample(producedWork);
			
			producedWork.setFormat(Format.A3);
			producedWork.setWorkType(WorkType.PRINTING_BLACK_AND_WHITE);
			Integer monochromeUsagesA3 = equipmentService.countOfUsagesByExample(producedWork);
			
			producedWork.setFormat(Format.A4);
			producedWork.setWorkType(WorkType.PRINTING_COLORED);
			Integer coloredUsagesA4 = equipmentService.countOfUsagesByExample(producedWork);
			
			producedWork.setFormat(Format.A4);
			producedWork.setWorkType(WorkType.PRINTING_BLACK_AND_WHITE);
			Integer monochromeUsagesA4 = equipmentService.countOfUsagesByExample(producedWork);
			
			equipmentFX.setColoredUsagesA3(coloredUsagesA3);
			equipmentFX.setMonochromeUsagesA3(monochromeUsagesA3);
			equipmentFX.setColoredUsagesA4(coloredUsagesA4);
			equipmentFX.setMonochromeUsagesA4(monochromeUsagesA4);
			
		}
	}
	
	
}
