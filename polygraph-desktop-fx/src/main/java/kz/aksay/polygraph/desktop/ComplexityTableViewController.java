package kz.aksay.polygraph.desktop;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

import javax.validation.ValidationException;

import kz.aksay.polygraph.api.IComplexityService;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Complexity;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.ComplexityFX;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;



public class ComplexityTableViewController implements Initializable, SessionAware {

	@FXML 	private TableView<ComplexityFX> tableView;
	@FXML	private Label validationLabel;
	
	@FXML	private TableColumn<ComplexityFX, String> nameColumn;
	@FXML 	private TableColumn<ComplexityFX, String> minColumn;
	@FXML 	private TableColumn<ComplexityFX, String> maxColumn;
	
	@FXML	private TextField nameField;
	@FXML	private TextField minField;
	@FXML   private TextField maxField;
	
	private Map<String, Object> session;
	
	private IComplexityService complexityService = StartingPane.getBean(IComplexityService.class);

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@FXML
	protected void add(ActionEvent event) {
		validationLabel.setText(null);
		
		try {
			
			Complexity complexity = createMaterial();
			
			save(complexity);
			
			ComplexityFX stickerFX = new ComplexityFX(complexity);
			tableView.getItems().add(stickerFX);
			nameField.setText(null);
		} catch(Exception e) {
			
			validationLabel.setText(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	protected void delete(ActionEvent event) {
		ComplexityFX ComplexityFX = tableView.getSelectionModel().getSelectedItem();
		if(ComplexityFX != null) {
			Complexity complexity = ComplexityFX.getEntity();
			try {
				complexityService.delete(complexity);
				tableView.getItems().remove(ComplexityFX);
			}
			catch(ValidationException ve) {
				validationLabel.setText(ve.getMessage());
			}
		}
	}
	
	@FXML
	protected void updateName(
			TableColumn.CellEditEvent<ComplexityFX, String> cellEditEvent) {
		ComplexityFX ComplexityFX = cellEditEvent.getRowValue();
		Complexity paper = ComplexityFX.getEntity();
		paper.setName(cellEditEvent.getNewValue());
		paper.setUpdatedAt(new Date());
		paper.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(paper);
	}
	
	@FXML
	protected void updateMin(
			TableColumn.CellEditEvent<ComplexityFX, String> cellEditEvent) {
		try {
			ComplexityFX ComplexityFX = cellEditEvent.getRowValue();
			Complexity material = ComplexityFX.getEntity();
			material.setMin(Integer.valueOf( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	@FXML
	protected void updateMax(
			TableColumn.CellEditEvent<ComplexityFX, String> cellEditEvent) {
		try {
			ComplexityFX ComplexityFX = cellEditEvent.getRowValue();
			Complexity material = ComplexityFX.getEntity();
			material.setMax(Integer.valueOf( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	protected void save(Complexity spring) {
		try {
			complexityService.save(spring);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Complexity createMaterial() throws Exception {
		Complexity mt = new Complexity();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setName(nameField.getText());		
		
		try {
			mt.setMin(Integer.valueOf( minField.getText() ));
		} catch (NumberFormatException nfe) {
			throw new Exception("Минимальное количество часов должно быть целым числом больше нуля!");
		}
		
		try {
			mt.setMax(Integer.valueOf(maxField.getText()));
		} catch(NumberFormatException nfe) {
			throw new Exception("Максимальное количество часов должно быть целым числом больше нуля!");
		}
		
		return mt;
	}

	public TableView<ComplexityFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<ComplexityFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<ComplexityFX> complexitiesFX = EntityFX.convertListEntityToFX(
				complexityService.findAll(), ComplexityFX.class);
		tableView.getItems().addAll(complexitiesFX);
		nameColumn.setCellFactory(TextFieldTableCell.<ComplexityFX>forTableColumn());
		minColumn.setCellFactory(TextFieldTableCell.<ComplexityFX>forTableColumn());
		maxColumn.setCellFactory(TextFieldTableCell.<ComplexityFX>forTableColumn());
	}
	

}
