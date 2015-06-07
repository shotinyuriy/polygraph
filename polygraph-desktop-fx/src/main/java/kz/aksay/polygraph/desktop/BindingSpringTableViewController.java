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

import kz.aksay.polygraph.api.IBindingSpringService;
import kz.aksay.polygraph.entity.BindingSpring;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.BindingSpringFX;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class BindingSpringTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<BindingSpringFX> tableView;
	
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<BindingSpringFX, String> descriptionColumn;
	@FXML	private TableColumn<BindingSpringFX, BindingSpring.Type> typeColumn;
	@FXML 	private TableColumn<BindingSpringFX, String> diameterColumn;
	@FXML 	private TableColumn<BindingSpringFX, String> priceColumn;
	@FXML	private TextField descriptionField;
	@FXML	private ComboBox<BindingSpring.Type> typeBox;
	@FXML	private TextField diameterField;
	@FXML   private TextField priceField;
	
	private Map<String, Object> session;
	
	private IBindingSpringService bindingSpringService 
		= ContextUtils.getBean(IBindingSpringService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		validationLabel.setText(null);
		
		try {
			
			ObservableList<BindingSpringFX> data = tableView.getItems();
			BindingSpring.Type type = typeBox.getValue();
			BindingSpring material = createMaterial(descriptionField.getText(), type);
			
			save(material);
			
			BindingSpringFX bindingSpringFX = new BindingSpringFX(material);
			data.add(bindingSpringFX);
			descriptionField.setText("");
			typeBox.setValue(null);
		} catch(Exception e) {
			
			validationLabel.setText(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	protected void delete(ActionEvent event) {
		BindingSpringFX bindingSpringFX = tableView.getSelectionModel().getSelectedItem();
		if(bindingSpringFX != null) {
			BindingSpring material = bindingSpringFX.getEntity();
			try {
				bindingSpringService.delete(material);
				tableView.getItems().remove(bindingSpringFX);
			}
			catch(ValidationException ve) {
				validationLabel.setText(ve.getMessage());
			}
		}
	}
	
	@FXML
	protected void updateDescription(
			TableColumn.CellEditEvent<BindingSpringFX, String> cellEditEvent) {
		BindingSpringFX bindingSpringFX = cellEditEvent.getRowValue();
		BindingSpring paper = bindingSpringFX.getEntity();
		paper.setDescription(cellEditEvent.getNewValue());
		paper.setUpdatedAt(new Date());
		paper.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(paper);
	}
	
	@FXML
	protected void updateType(
			TableColumn.CellEditEvent<BindingSpringFX, BindingSpring.Type> cellEditEvent) {
		
		BindingSpringFX bindingSpringFX = cellEditEvent.getRowValue();
		BindingSpring material = bindingSpringFX.getEntity();
		material.setType(cellEditEvent.getNewValue());
		material.setUpdatedAt(new Date());
		material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(material);
	}
	
	@FXML
	protected void updateDiameter(
			TableColumn.CellEditEvent<BindingSpringFX, String> cellEditEvent) {
		try {
			BindingSpringFX bindingSpringFX = cellEditEvent.getRowValue();
			BindingSpring material = bindingSpringFX.getEntity();
			material.setDiameter(Integer.valueOf( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	@FXML
	protected void updatePrice(
			TableColumn.CellEditEvent<BindingSpringFX, String> cellEditEvent) {
		try {
			BindingSpringFX bindingSpringFX = cellEditEvent.getRowValue();
			BindingSpring material = bindingSpringFX.getEntity();
			material.setPrice(new BigDecimal( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	protected void save(BindingSpring spring) {
		try {
			bindingSpringService.save(spring);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected BindingSpring createMaterial(String name, BindingSpring.Type type) throws Exception {
		BindingSpring mt = new BindingSpring();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setDescription(name.toString());
		mt.setType(type);
		
		try {
			mt.setDiameter(Integer.valueOf( diameterField.getText() ));
		} catch (NumberFormatException nfe) {
			throw new Exception("Плотность должна быть целым числом больше нуля");
		}
		
		try {
			mt.setPrice(new BigDecimal(priceField.getText()));
		} catch(NumberFormatException nfe) {
			throw new Exception("Цена должна быть числом большим или равным нуля");
		}
		
		return mt;
	}

	public TableView<BindingSpringFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<BindingSpringFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		
		
		List<BindingSpringFX> materialTypesFX 
			= EntityFX.convertListEntityToFX(bindingSpringService.findAll(), BindingSpringFX.class);
		
		tableView.getItems().setAll(materialTypesFX);
		
		typeBox.getItems().addAll(BindingSpring.Type.values());
		
		diameterColumn.setCellFactory(
				TextFieldTableCell.<BindingSpringFX>forTableColumn());
		descriptionColumn.setCellFactory(
				TextFieldTableCell.<BindingSpringFX>forTableColumn());
		priceColumn.setCellFactory(
				TextFieldTableCell.<BindingSpringFX>forTableColumn());
		typeColumn.setCellFactory(
				ComboBoxTableCell.<BindingSpringFX, BindingSpring.Type>forTableColumn(typeBox.getItems()));
		
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
