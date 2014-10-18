package kz.aksay.polygraph.fxapi;

import java.io.Serializable;
import java.lang.reflect.Field;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import kz.aksay.polygraph.service.GenericService;

public class GenericFormController<T extends Serializable, PK extends Serializable> {
	
	protected GenericService<T, PK> service;

	@FXML
	public void save(ActionEvent actionEvent) {
		
	}
	
	public void fill(PK id) {
		T entity = service.find(id);
		if(entity != null) {
			Class<?> controllerClass = getClass();
			for(Field field : controllerClass.getDeclaredFields()) {
				
			}
		}
	}
}
