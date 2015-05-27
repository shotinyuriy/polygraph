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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.entityfx.PaperFX;
import kz.aksay.polygraph.entityfx.PaperTypeFX;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.service.PaperTypeService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class PaperTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<PaperFX> tableView;
	
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<PaperFX, String> nameColumn;
	@FXML 	private TableColumn<PaperFX, Format> formatColumn;
	@FXML	private TableColumn<PaperFX, PaperTypeFX> materialTypeColumn;
	@FXML 	private TableColumn<PaperFX, String> densityColumn;
	@FXML	private TextField nameField;
	@FXML 	private ComboBox<Format> formatBox;
	@FXML	private ComboBox<PaperTypeFX> paperTypeBox;
	@FXML	private TextField densityField;
	
	private Map<String, Object> session;
	
	private IPaperTypeService paperTypeService 
		= ContextUtils.getBean(IPaperTypeService.class);
	
	private IPaperService paperService 
		= ContextUtils.getBean(IPaperService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		validationLabel.setText(null);
		
		try {
			ObservableList<PaperFX> data = tableView.getItems();
			PaperTypeFX materialTypeFx = paperTypeBox.getValue();
			PaperType paperType = materialTypeFx.getPaperType();
			Paper material = createMaterial(nameField.getText(), paperType);
			material.setFormat(formatBox.getValue());
			
			try {
			
				material.setDensity(Integer.valueOf( densityField.getText() ));
			} catch (NumberFormatException nfe) {
				throw new Exception("Плотность должна быть целым числом больше нуля");
			}
			
			save(material);
			PaperFX paperFX = new PaperFX(material);
			data.add(paperFX);
			nameField.setText("");
			paperTypeBox.setValue(null);
		} catch(Exception e) {
			
			validationLabel.setText(e.getMessage());
		}
	}
	
	@FXML
	protected void delete(ActionEvent event) {
		PaperFX PaperFX = tableView.getSelectionModel().getSelectedItem();
		if(PaperFX != null) {
			Paper material = PaperFX.getEntity();
			try {
				paperService.delete(material);
				tableView.getItems().remove(PaperFX);
			}
			catch(ValidationException ve) {
				validationLabel.setText(ve.getMessage());
			}
		}
	}
	
	@FXML
	protected void updateName(
			TableColumn.CellEditEvent<PaperFX, String> cellEditEvent) {
		PaperFX PaperFX = cellEditEvent.getRowValue();
		Paper paper = PaperFX.getEntity();
		paper.setDescription(cellEditEvent.getNewValue());
		paper.setUpdatedAt(new Date());
		paper.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(paper);
	}
	
	@FXML
	protected void updateType(
			TableColumn.CellEditEvent<PaperFX, PaperTypeFX> cellEditEvent) {
		
		PaperFX PaperFX = cellEditEvent.getRowValue();
		Paper material = PaperFX.getEntity();
		material.setType(cellEditEvent.getNewValue().getEntity());
		material.setUpdatedAt(new Date());
		material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(material);
	}
	
	@FXML
	protected void updateFormat(
			TableColumn.CellEditEvent<PaperFX, Format> cellEditEvent) {
		
		PaperFX PaperFX = cellEditEvent.getRowValue();
		Paper material = PaperFX.getEntity();
		material.setFormat(cellEditEvent.getNewValue());
		material.setUpdatedAt(new Date());
		material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(material);
	}
	
	@FXML
	protected void updateDensity(
			TableColumn.CellEditEvent<PaperFX, String> cellEditEvent) {
		try {
			PaperFX PaperFX = cellEditEvent.getRowValue();
			Paper material = PaperFX.getEntity();
			material.setDensity(Integer.valueOf( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	protected void save(Paper paper) {
		try {
			paperService.save(paper);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Paper createMaterial(String name, PaperType paperType) {
		Paper mt = new Paper();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setDescription(name.toString());
		mt.setType(paperType);
		return mt;
	}

	public TableView<PaperFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<PaperFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		List<PaperFX> papersFX 
			= EntityFX.convertListEntityToFX(paperService.findAll(), PaperFX.class);
		tableView.getItems().setAll(papersFX);
		
		List<PaperTypeFX> materialTypesFX 
			= EntityFX.convertListEntityToFX(paperTypeService.findAll(), PaperTypeFX.class);
		paperTypeBox.getItems().setAll(materialTypesFX);
		
		formatBox.getItems().addAll(Format.values());
		
		nameColumn.setCellFactory(
				TextFieldTableCell.<PaperFX>forTableColumn());
		densityColumn.setCellFactory(
				TextFieldTableCell.<PaperFX>forTableColumn());
		formatColumn.setCellFactory(
				ComboBoxTableCell.<PaperFX, Format>forTableColumn(Format.values()));
		materialTypeColumn.setCellFactory(
				ComboBoxTableCell.<PaperFX, PaperTypeFX>forTableColumn(paperTypeBox.getItems()));
		
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
