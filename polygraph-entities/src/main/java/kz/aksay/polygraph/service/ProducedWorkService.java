package kz.aksay.polygraph.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.dao.GenericDao;

@Service
public class ProducedWorkService extends GenericService<ProducedWork, Long> {
	
	private GenericDao<ProducedWork, Long> producedWorkDao;

	@Transactional(readOnly=true)
	public List<ProducedWork> findAllByOrderId(Long orderId) {
		Criteria criteria = getDao().getSession().createCriteria(getDao().clazz());
		criteria.add(Restrictions.eq("order.id", orderId));
		return getDao().readByCriteria(criteria);
	}
	
	@Override
	protected GenericDao<ProducedWork, Long> getDao() {
		return producedWorkDao;
	}

	@Autowired
	public void setProducedWorkDao(GenericDao<ProducedWork, Long> producedWorkDao) {
		this.producedWorkDao = producedWorkDao;
	}
}
