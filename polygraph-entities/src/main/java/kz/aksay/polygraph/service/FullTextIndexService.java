package kz.aksay.polygraph.service;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.FullTextIndex;

@Service
public class FullTextIndexService extends GenericService<FullTextIndex, Long> {

	private GenericDao<FullTextIndex, Long> fullTextIndexDao;

	public FullTextIndex findByText(String text) {
		Criteria criteria = getDao().getSession().createCriteria(FullTextIndex.class);
		criteria.add(Restrictions.ilike("text", text, MatchMode.EXACT));
		return getDao().readUniqueByCriteria(criteria);
	}
	
/*	@Transactional
	public int deleteAll() {
		int rowsAffected = 0;
		Query query = getDao().getSession().createQuery(
				"DELETE FROM FullTextIndex");
		rowsAffected = query.executeUpdate();
		return rowsAffected;
	}*/
	
	@Override
	@Transactional
	public FullTextIndex save(FullTextIndex fullTextIndex) throws Exception {
		fullTextIndex.setText(fullTextIndex.getText().toLowerCase());
		return super.save(fullTextIndex);
	}
	
	@Override
	protected GenericDao<FullTextIndex, Long> getDao() {
		return fullTextIndexDao;
	}
	
	@Autowired
	public void setFullTextIndexDao(GenericDao<FullTextIndex, Long> fullTextIndexDao) {
		this.fullTextIndexDao = fullTextIndexDao;
	}

}
