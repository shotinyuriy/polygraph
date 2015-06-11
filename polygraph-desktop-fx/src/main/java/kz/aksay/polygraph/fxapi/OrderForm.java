package kz.aksay.polygraph.fxapi;

import javafx.scene.control.TableView;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;

public interface OrderForm {
	public void addProducedWork(ProducedWorkFX producedWorkFX);
	public void saveProducedWork(ProducedWorkFX producedWorkFX);
	public void openNewProducedWorkForm();
	public void refreshTotalCost();
	public EmployeeFX getCurrentExecutor();
	public void setMaterialConsumptionTableView(
			TableView<MaterialConsumptionFX> tableView);
}
