package kz.aksay.polygraph.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.PaperType;

@Service
public class PaperService extends AbstractGenericService<Paper, Long> implements IPaperService {

	private GenericDao<Paper, Long> paperDao; 
	
	@Override
	protected GenericDao<Paper, Long> getDao() {
		return paperDao;
	}
	
	@Autowired
	public void setPaperDao(GenericDao<Paper, Long> paperDao) {
		this.paperDao = paperDao;
	}

	@Override
	public int deleteAllByPaperType(PaperType paperType) {
		Query query = getDao().getSession().createQuery(
				"DELETE FROM Paper pt WHERE pt.type = :paperType");
		query.setParameter("paperType", paperType);
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Paper> findByExampleAndPaperType(Paper example,
			PaperType paperType) {
		
		Criteria criteria = getDao().getSession().createCriteria(getDao().clazz())
				.add(Restrictions.eq("type", paperType));
		
		if(example.getDensity() != null) {
			criteria.add(Restrictions.eq("density", example.getDensity()));
		}
		
		if(example.getFormat() != null) {
			criteria.add(Restrictions.eq("format", example.getFormat()));
		}
		
		return criteria.list();
	}

	@Override
	public Set<Format> findAvailableFormats() {
		Set<Format> formats = new HashSet<Format>();
		
		for(Paper paper : findAll()) {
			formats.add(paper.getFormat());
		}
		
		return formats;
	}

	
}
