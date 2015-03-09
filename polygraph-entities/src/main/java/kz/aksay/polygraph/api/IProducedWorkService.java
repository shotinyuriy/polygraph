package kz.aksay.polygraph.api;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.dao.GenericDao;

public interface IProducedWorkService extends IGenericService<ProducedWork, Long> {

	public List<ProducedWork> findAllByOrderId(Long orderId);
	
	public ProducedWork save(ProducedWork producedWork) throws Exception;
	
	public int deleteAllByOrder(Order order);

}
