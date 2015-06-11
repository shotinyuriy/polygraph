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

import kz.aksay.polygraph.api.ILaminateService;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Laminate;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.entityfx.LaminateFX;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;



public class LaminateTableViewController implements Initializable, SessionAware {

	@FXML 	private TableView<LaminateFX> tableView;
	@FXML	private Label validationLabel;
	
	@FXML	private TableColumn<LaminateFX, String> descriptionColumn;
	@FXML 	private TableColumn<LaminateFX, Format> formatColumn;
	@FXML 	private TableColumn<LaminateFX, String> priceColumn;
	@FXML 	private TableColumn<LaminateFX, String> densityColumn;
	
	@FXML	private TextField descriptionField;
	@FXML 	private ComboBox<Format> formatBox;
	@FXML	private TextField densityField;
	@FXML   private TextField priceField;
	
	private Map<String, Object> session;
	
	private ILaminateService LaminateService = StartingPane.getBean(ILaminateService.class);

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@FXML
	protected void add(ActionEvent event) {
		validationLabel.setText(null);
		
		try {
			
			Laminate material = createMaterial();
			
			save(material);
			
			LaminateFX LaminateFX = new LaminateFX(material);
			tableView.getItems().add(LaminateFX);
			descriptionField.setText(null);
			formatBox.setValue(null);
		} catch(Exception e) {
			
			validationLabel.setText(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	protected void delete(ActionEvent event) {
		LaminateFX LaminateFX = tableView.getSelectionModel().getSelectedItem();
		if(LaminateFX != null) {
			Laminate material = LaminateFX.getEntity();
			try {
				LaminateService.delete(material);
				tableView.getItems().remove(LaminateFX);
			}
			catch(ValidationException ve) {
				validationLabel.setText(ve.getMessage());
			}
		}
	}
	
	@FXML
	protected void updateDescription(
			TableColumn.CellEditEvent<LaminateFX, String> cellEditEvent) {
		LaminateFX LaminateFX = cellEditEvent.getRowValue();
		Laminate paper = LaminateFX.getEntity();
		paper.setDescription(cellEditEvent.getNewValue());
		paper.setUpdatedAt(new Date());
		paper.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(paper);
	}
	
	@FXML
	protected void updateFormat(
			TableColumn.CellEditEvent<LaminateFX, Format> cellEditEvent) {
		try {
			LaminateFX LaminateFX = cellEditEvent.getRowValue();
			Laminate material = LaminateFX.getEntity();
			material.setFormat(cellEditEvent.getNewValue());
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	@FXML
	protected void updatePrice(
			TableColumn.CellEditEvent<LaminateFX, String> cellEditEvent) {
		try {
			LaminateFX LaminateFX = cellEditEvent.getRowValue();
			Laminate material = LaminateFX.getEntity();
			material.setPrice(new BigDecimal( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	@FXML
	protected void updateDensity(
			TableColumn.CellEditEvent<LaminateFX, String> cellEditEvent) {
		try {
			LaminateFX LaminateFX = cellEditEvent.getRowValue();
			Laminate material = LaminateFX.getEntity();
			material.setDensity(Integer.valueOf( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	protected void save(Laminate spring) {
		try {
			LaminateService.save(spring);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Laminate createMaterial() throws Exception {
		Laminate mt = new Laminate();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setDescription(descriptionField.getText());
		mt.setFormat(formatBox.getSelectionModel().getSelectedItem());
		
		try {
			mt.setDensity(Integer.valueOf( densityField.getText() ));
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

	public TableView<LaminateFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<LaminateFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<LaminateFX> LaminatesFX = EntityFX.convertListEntityToFX(
				LaminateService.findAll(), LaminateFX.class);
		tableView.getItems().addAll(LaminatesFX);
		formatBox.getItems().addAll(Format.values());
		
		descriptionColumn.setCellFactory(TextFieldTableCell.<LaminateFX>forTableColumn());
		formatColumn.setCellFactory(ComboBoxTableCell.<LaminateFX, Format>forTableColumn(Format.values()));
		priceColumn.setCellFactory(TextFieldTableCell.<LaminateFX>forTableColumn());
		densityColumn.setCellFactory(TextFieldTableCell.<LaminateFX>forTableColumn());
	}
	

}
