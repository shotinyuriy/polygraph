package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entityfx.WorkTypeFX;
import kz.aksay.polygraph.service.WorkTypeService;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class WorkTypeTableViewController implements SessionAware, Initializable {

	private WorkTypeService workTypeService;
	private Map<String, Object> session;
	
	@FXML private TableColumn<WorkTypeFX, String> nameColumn;
	@FXML private TableView<WorkTypeFX> tableView;
	@FXML private TextField nameField;
	@FXML private Label validationLabel;
	
	@FXML
	protected void add(ActionEvent actionEvent) {
		try {
			validationLabel.setText(null);
			String name = nameField.getText();
			if(name != null && !name.trim().isEmpty()) { 
				WorkType workType = new WorkType();
				workType.setName(name.toUpperCase());
				workType.setCreatedAt(new Date());
				workType.setCreatedBy(SessionUtil.retrieveUser(session));
				workType = workTypeService.save(workType);
				WorkTypeFX workTypeFX = new WorkTypeFX(workType);
				tableView.getItems().add(workTypeFX);
				
				nameField.setText(null);
			}
			else {
				throw new Exception("Название не может быть пустым");
			}
		}
		catch(Exception e) {
			validationLabel.setText(e.getMessage());
		}
	}
	
	@FXML
	protected void update(
			TableColumn.CellEditEvent<WorkTypeFX, String> cellEditEvent) {
		try {
			WorkTypeFX workTypeFX = cellEditEvent.getRowValue();
			WorkType workType = workTypeFX.getWorkType(); 
			workType.setName(cellEditEvent.getNewValue());
			workType.setUpdatedAt(new Date());
			workType.setUpdatedBy(SessionUtil.retrieveUser(session));
			
			workTypeService.save(workType);
		}
		catch(Exception e) {
			validationLabel.setText(e.getMessage());
		}
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		workTypeService = StartingPane.getBean(WorkTypeService.class);
		
		List<WorkType> workTypes = workTypeService.findAll();
		tableView.getItems().addAll(
				WorkTypeFX.convertListEntityToFX(workTypes));
		
		nameColumn.setCellFactory(
				TextFieldTableCell.<WorkTypeFX>forTableColumn());
	}
	
}
