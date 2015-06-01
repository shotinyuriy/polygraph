package kz.aksay.polygraph.desktop;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.desktop.controls.AutoCompleteComboBoxListener;
import kz.aksay.polygraph.entity.MaterialConsumer;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.MaterialFX;
import kz.aksay.polygraph.exception.InternalLogicException;
import kz.aksay.polygraph.fxapi.MaterialConsumptionForm;
import kz.aksay.polygraph.fxapi.MaterialConsumptionHolderFX;
import kz.aksay.polygraph.fxapi.OrderForm;
import kz.aksay.polygraph.fxapi.ProducedWorkForm;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MaterialConsumptionTableViewController implements ParametersAware,
		SessionAware, Initializable, MaterialConsumptionForm {

	private Map<String, Object> session; 

	@FXML private ComboBox<MaterialFX> materialCombo;
	@FXML private TextField quantityField;
	@FXML private TableView<MaterialConsumptionFX> materialConsumptionsTableView;
	@FXML private Label validationLabel;
	@FXML private HBox controlPanel;

	private IMaterialService materialService;
	private IMaterialConsumptionService materialConsumptionService;
	
	private final Collection<MaterialFX> materialsFX = new ArrayList<MaterialFX>();
	private AutoCompleteComboBoxListener autoCompleteMaterial;
	
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
			
			if(materialFX == null) throw new InternalLogicException("Необходимо указать материал");
			materialConsumptionFX.setMaterialFX(materialFX);
			materialConsumptionFX.setQuantity(retrieveQuantity());
			materialConsumptionsTableView.getItems().add(materialConsumptionFX);
			
			clearForm();
		}
		catch(InternalLogicException ie) {
			validationLabel.setText(ie.getMessage());
		} catch(Exception e) {
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
					materialConsumptionFXToRemove.getEntity());
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
	public void loadMaterialsByWorkType(WorkType workType) {
		
		materialsFX.clear();
		materialsFX.addAll(
			MaterialFX.convertListEntityToFX(
					materialService.findMaterialsByWorkType(workType)));
		materialCombo.getItems().clear();
		materialCombo.getItems().addAll(materialsFX);
		autoCompleteMaterial.refreshData();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		materialService = StartingPane.getBean(IMaterialService.class);
		materialConsumptionService = StartingPane.getBean(IMaterialConsumptionService.class);
		materialsFX.addAll( 
			MaterialFX.convertListEntityToFX(materialService.findAll()));
		materialCombo.getItems().addAll(materialsFX);
		
		materialCombo.converterProperty().set(new StringConverter<MaterialFX>() {

			@Override
			public String toString(MaterialFX object) {
				if(object != null)
					return object.getName();
				return null;
			}

			@Override
			public MaterialFX fromString(String string) {
				MaterialFX materialFX = null;
				for(MaterialFX currrentMaterialFX : materialsFX) {
					if(currrentMaterialFX.getName().equals(string) ) {
						materialFX = currrentMaterialFX;
					}
				}
				return materialFX;
			}
			
		});
		
		autoCompleteMaterial = 
				new AutoCompleteComboBoxListener<MaterialFX>(materialCombo);
	}
	
	
	private void initializeByParameters(Map<String, Object> parameters) {
		MaterialConsumptionHolderFX materialConsumer = ParametersUtil.
				extractParameter(parameters, 
				ParameterKeys.MATERIAL_CONSUMER, 
				MaterialConsumptionHolderFX.class);
		ProducedWorkForm producedWorkForm = ParametersUtil.
				extractParameter(parameters, 
						ParameterKeys.PRODUCED_WORK_FORM, 
						ProducedWorkForm.class);
		
		if(materialConsumer != null) {
			materialConsumptionsTableView.getItems().addAll(
					materialConsumer.getMaterialConsumptionFX());
			materialConsumer.setMaterialConsumptionFX(
					materialConsumptionsTableView.getItems());
		}
		
		OrderForm orderForm = ParametersUtil.extractParameter(
				parameters, ParameterKeys.ORDER_FORM, OrderForm.class);
		if(orderForm != null) {
			orderForm.setMaterialConsumptionTableView(materialConsumptionsTableView);
			controlPanel.setVisible(false);
		}
		
		if(producedWorkForm != null) {
			producedWorkForm.setMaterialConsumptionForm(this);
		}
	}

}
