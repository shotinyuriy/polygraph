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

import kz.aksay.polygraph.api.IStickerService;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Sticker;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.entityfx.StickerFX;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;



public class StickerTableViewController implements Initializable, SessionAware {

	@FXML 	private TableView<StickerFX> tableView;
	@FXML	private Label validationLabel;
	
	@FXML	private TableColumn<StickerFX, String> descriptionColumn;
	@FXML	private TableColumn<StickerFX, Sticker.Type> typeColumn;
	@FXML 	private TableColumn<StickerFX, Format> formatColumn;
	@FXML 	private TableColumn<StickerFX, String> priceColumn;
	@FXML 	private TableColumn<StickerFX, String> densityColumn;
	
	@FXML	private TextField descriptionField;
	@FXML 	private ComboBox<Format> formatBox;
	@FXML	private ComboBox<Sticker.Type> typeBox;
	@FXML	private TextField densityField;
	@FXML   private TextField priceField;
	
	private Map<String, Object> session;
	
	private IStickerService stickerService = StartingPane.getBean(IStickerService.class);

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@FXML
	protected void add(ActionEvent event) {
		validationLabel.setText(null);
		
		try {
			
			Sticker material = createMaterial();
			
			save(material);
			
			StickerFX stickerFX = new StickerFX(material);
			tableView.getItems().add(stickerFX);
			descriptionField.setText(null);
			typeBox.setValue(null);
			formatBox.setValue(null);
		} catch(Exception e) {
			
			validationLabel.setText(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	protected void delete(ActionEvent event) {
		StickerFX StickerFX = tableView.getSelectionModel().getSelectedItem();
		if(StickerFX != null) {
			Sticker material = StickerFX.getEntity();
			try {
				stickerService.delete(material);
				tableView.getItems().remove(StickerFX);
			}
			catch(ValidationException ve) {
				validationLabel.setText(ve.getMessage());
			}
		}
	}
	
	@FXML
	protected void updateDescription(
			TableColumn.CellEditEvent<StickerFX, String> cellEditEvent) {
		StickerFX StickerFX = cellEditEvent.getRowValue();
		Sticker paper = StickerFX.getEntity();
		paper.setDescription(cellEditEvent.getNewValue());
		paper.setUpdatedAt(new Date());
		paper.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(paper);
	}
	
	@FXML
	protected void updateType(
			TableColumn.CellEditEvent<StickerFX, Sticker.Type> cellEditEvent) {
		
		StickerFX StickerFX = cellEditEvent.getRowValue();
		Sticker material = StickerFX.getEntity();
		material.setStickerType(cellEditEvent.getNewValue());
		material.setUpdatedAt(new Date());
		material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(material);
	}
	
	@FXML
	protected void updateFormat(
			TableColumn.CellEditEvent<StickerFX, Format> cellEditEvent) {
		try {
			StickerFX StickerFX = cellEditEvent.getRowValue();
			Sticker material = StickerFX.getEntity();
			material.setFormat(cellEditEvent.getNewValue());
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	@FXML
	protected void updatePrice(
			TableColumn.CellEditEvent<StickerFX, String> cellEditEvent) {
		try {
			StickerFX StickerFX = cellEditEvent.getRowValue();
			Sticker material = StickerFX.getEntity();
			material.setPrice(new BigDecimal( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	@FXML
	protected void updateDensity(
			TableColumn.CellEditEvent<StickerFX, String> cellEditEvent) {
		try {
			StickerFX StickerFX = cellEditEvent.getRowValue();
			Sticker material = StickerFX.getEntity();
			material.setDensity(Integer.valueOf( cellEditEvent.getNewValue() ));
			material.setUpdatedAt(new Date());
			material.setUpdatedBy(SessionUtil.retrieveUser(session));
		
			save(material);
		} catch (NumberFormatException nfe) {
			
		}
	}
	
	protected void save(Sticker spring) {
		try {
			stickerService.save(spring);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Sticker createMaterial() throws Exception {
		Sticker mt = new Sticker();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setDescription(descriptionField.getText());
		mt.setStickerType(typeBox.getValue());
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

	public TableView<StickerFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<StickerFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<StickerFX> stickersFX = EntityFX.convertListEntityToFX(
				stickerService.findAll(), StickerFX.class);
		tableView.getItems().addAll(stickersFX);
		formatBox.getItems().addAll(Format.values());
		typeBox.getItems().addAll(Sticker.Type.values());
		
		descriptionColumn.setCellFactory(TextFieldTableCell.<StickerFX>forTableColumn());
		typeColumn.setCellFactory(ComboBoxTableCell.<StickerFX, Sticker.Type>forTableColumn(Sticker.Type.values()));;
		formatColumn.setCellFactory(ComboBoxTableCell.<StickerFX, Format>forTableColumn(Format.values()));
		priceColumn.setCellFactory(TextFieldTableCell.<StickerFX>forTableColumn());
		densityColumn.setCellFactory(TextFieldTableCell.<StickerFX>forTableColumn());
	}
	

}
