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
import kz.aksay.polygraph.entityfx.MaterialTypeFX;
import kz.aksay.polygraph.service.PaperTypeService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MaterialTypeTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<MaterialTypeFX> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	@FXML	private TableColumn<MaterialTypeFX, String> nameColumn;
	
	private Map<String, Object> session;
	
	private IPaperTypeService paperTypeService 
		= ContextUtils.getBean(IPaperTypeService.class);
	
	@FXML
	protected void add(ActionEvent event) {
		ObservableList<MaterialTypeFX> data = tableView.getItems();
		PaperType newMaterialType = createMaterialType(nameField.getText());
		MaterialTypeFX materialTypeFX = new MaterialTypeFX(newMaterialType);
		data.add(materialTypeFX);
		save(newMaterialType);
		nameField.setText("");
	}
	
	@FXML
	protected void update(
			TableColumn.CellEditEvent<MaterialTypeFX, String> cellEditEvent) {
		MaterialTypeFX materialTypeFX = cellEditEvent.getRowValue();
		PaperType paperType = materialTypeFX.getMaterialType();
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

	public TableView<MaterialTypeFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<MaterialTypeFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		List<MaterialTypeFX> materialTypesFX 
			= MaterialTypeFX.convertListEntityToFX(paperTypeService.findAll());
		tableView.getItems().setAll(materialTypesFX);
		nameColumn.setCellFactory(
				TextFieldTableCell.<MaterialTypeFX>forTableColumn());
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
