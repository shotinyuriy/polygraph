package kz.aksay.polygraph.desktop.controls;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

public class IndefiniteProgressMaker extends Task<Void> {
	
	private ProgressIndicator progressIndicator;
	
	public IndefiniteProgressMaker(ProgressIndicator progressIndicator) {
		this.progressIndicator = progressIndicator;
	}

	@Override
	protected Void call() throws Exception {
		
		double max = 1;
		double current = 0.1;
		while(!this.isCancelled() && current <= max) {
			updateProgress(current, max);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			current+=0.1;
		}
		return null;
	}
	
	@Override
	protected void updateProgress(double workDone, double max) {
		if(workDone == 0.1) {
			progressIndicator.setVisible(true);
		}
		progressIndicator.setProgress(workDone);
	}
}
