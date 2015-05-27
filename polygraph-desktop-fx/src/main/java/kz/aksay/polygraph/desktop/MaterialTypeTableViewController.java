package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.List;
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
import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.entityfx.PaperTypeFX;
import kz.aksay.polygraph.service.PaperTypeService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MaterialTypeTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<PaperTypeFX> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<PaperTypeFX, String> nameColumn;
	
	private Map<String, Object> session;
	
	private IPaperTypeService paperTypeService 
		= ContextUtils.getBean(IPaperTypeService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		ObservableList<PaperTypeFX> data = tableView.getItems();
		PaperType newMaterialType = createMaterialType(nameField.getText());
		PaperTypeFX paperTypeFX = new PaperTypeFX(newMaterialType);
		data.add(paperTypeFX);
		save(newMaterialType);
		nameField.setText("");
	}
	
	@FXML
	protected void update(
			TableColumn.CellEditEvent<PaperTypeFX, String> cellEditEvent) {
		PaperTypeFX paperTypeFX = cellEditEvent.getRowValue();
		PaperType paperType = paperTypeFX.getPaperType();
		paperType.setName(cellEditEvent.getNewValue());
		paperType.setUpdatedAt(new Date());
		paperType.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(paperType);
	}
	
	protected void save(PaperType paperType) {
		try {
			paperTypeService.save(paperType);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected PaperType createMaterialType(String name) {
		PaperType mt = new PaperType();
		mt.setCreatedAt(new Date());
		mt.setCreatedBy(User.TECH_USER);
		mt.setName(name.toString());
		return mt;
	}

	public TableView<PaperTypeFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<PaperTypeFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		List<PaperTypeFX> materialTypesFX 
			= EntityFX.convertListEntityToFX(paperTypeService.findAll(), PaperTypeFX.class);
		tableView.getItems().setAll(materialTypesFX);
		nameColumn.setCellFactory(
				TextFieldTableCell.<PaperTypeFX>forTableColumn());
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
