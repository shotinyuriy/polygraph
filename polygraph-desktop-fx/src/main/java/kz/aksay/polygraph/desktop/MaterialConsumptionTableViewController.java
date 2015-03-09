package kz.aksay.polygraph.desktop;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.entity.MaterialConsumer;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.MaterialFX;
import kz.aksay.polygraph.fxapi.MaterialConsumptionHolderFX;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MaterialConsumptionTableViewController implements ParametersAware,
		SessionAware, Initializable {

	private Map<String, Object> session; 

	@FXML private ComboBox<MaterialFX> materialCombo;
	@FXML private TextField quantityField;
	@FXML private TableView<MaterialConsumptionFX> materialConsumptionsTableView;
	@FXML private Label validationLabel;

	private IMaterialService materialService;
	private IMaterialConsumptionService materialConsumptionService;
	
	 
	
	private void initializeByParameters(Map<String, Object> parameters) {
		MaterialConsumptionHolderFX materialConsumer = ParametersUtil.
				extractParameter(parameters, 
				ParameterKeys.MATERIAL_CONSUMER, 
				MaterialConsumptionHolderFX.class);
		
		if(materialConsumer != null) {
			materialConsumptionsTableView.getItems().addAll(materialConsumer.getMaterialConsumptionFX());
			materialConsumer.setMaterialConsumptionFX(materialConsumptionsTableView.getItems());
		}
	}
	
	@FXML
	public void addConsumption(ActionEvent actionEvent) {
		try {
			MaterialConsumption materialConsumption = new MaterialConsumption();
			materialConsumption.setCreatedAt(new Date());
			materialConsumption.setCreatedBy(SessionUtil.retrieveUser(session));
			MaterialConsumptionFX materialConsumptionFX 
				= new MaterialConsumptionFX(materialConsumption);
			materialConsumptionFX.setDirty(true);
			MaterialFX materialFX = materialCombo.getSelectionModel().
					getSelectedItem();
			materialConsumptionFX.setMaterialFX(materialFX);
			materialConsumptionFX.setQuantity(retrieveQuantity());
			materialConsumptionsTableView.getItems().add(materialConsumptionFX);
			
			clearForm();
		}
		catch(Exception e) {
			e.printStackTrace();
			validationLabel.setText(e.getMessage());
		}
	}
	
	private void clearForm() {
		materialCombo.getSelectionModel().select(null);
		quantityField.setText(null);
	}

	@FXML
	public void removeConsumption(ActionEvent actionEvent) {
		try {
			MaterialConsumptionFX materialConsumptionFXToRemove 
				= materialConsumptionsTableView.getSelectionModel()
					.getSelectedItem();
			materialConsumptionsTableView.getItems()
				.remove(materialConsumptionFXToRemove);
			
			if(!materialConsumptionFXToRemove.isDirty()) {
				materialConsumptionService.delete(
					materialConsumptionFXToRemove.getMaterialConsumption());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			validationLabel.setText(e.getMessage());
		}
	}
	
	private BigDecimal retrieveQuantity() throws Exception {
		String quantityText = quantityField.getText();
		if(quantityText != null) {
			quantityText = quantityText.trim();
			try {
				return new BigDecimal(quantityText);
			}
			catch(NumberFormatException e) {
				throw new NumberFormatException("Некорректно указано количество!");
			}
		}
		return BigDecimal.ZERO; 
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		initializeByParameters(parameters);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		materialService = StartingPane.getBean(IMaterialService.class);
		materialConsumptionService = StartingPane.getBean(IMaterialConsumptionService.class);
		Collection<MaterialFX> materialsFX 
			= MaterialFX.convertListEntityToFX(materialService.findAll());
		materialCombo.getItems().addAll(materialsFX);
	}

}
