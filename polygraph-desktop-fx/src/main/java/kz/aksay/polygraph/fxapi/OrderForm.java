package kz.aksay.polygraph.fxapi;

import kz.aksay.polygraph.entityfx.ProducedWorkFX;

public interface OrderForm {
	public void addProducedWork(ProducedWorkFX producedWorkFX);
	public void saveProducedWork(ProducedWorkFX producedWorkFX);
}
