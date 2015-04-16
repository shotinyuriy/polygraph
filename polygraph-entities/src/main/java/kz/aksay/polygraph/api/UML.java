package kz.aksay.polygraph.api;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Customer;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.FullTextIndex;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.OrderFullTextIndex;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.AbstractGenericService;

public interface UML {
	
}
	/*public static interface IGenericService {
		
		public T find(PK id);
		
		public List<T> findAll();
		
		public List<T> findAll(int offset, int limit);
		
		public T save(T entity) throws Exception;
		
		public void delete(T entity);
		
		public int deleteAll();
	}
	
	
	public interface IFullTextIndexService extends IGenericService {

		public FullTextIndex findByText(String text);
		
		@Override
		public FullTextIndex save(FullTextIndex fullTextIndex) throws Exception;
	}
	
	
	public interface IOrderFullTextIndexService extends IGenericService {
		
		public void recreateOrderFullTextIndexes(Order order) throws Exception;
		
		public List<OrderFullTextIndex> createOrderFullTextIndexes(Order order);
		
		public int deleteByOrder(Order order);
		 
		public void saveAll(List<OrderFullTextIndex> orderFullTextIndexes) throws Exception;

	}
	
	public interface IOrderService extends IGenericService {

		public Order find(Long id);
		
		public Order save(Order order) throws Exception;
		
		public List<Order> findByExample(Order example);
		
		public List<Order> findByExampleAndSearchString(Order example, String searchString);
		
	}
	
	public abstract class AbstractGenericService 
	implements IGenericService {
	
	protected abstract GenericDao<T, PK> getDao();
	
	@Override
	public T find(PK id) {
		return getDao().read(id);
	}
	
	@Override
	public List<T> findAll() {
		return getDao().readAll();
	}
	
	@Override
	public List<T> findAll(int offset, int limit) {
		return getDao().readAll(offset, limit);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public T save(T entity) throws Exception {
		
		getDao().createOrUpdate(entity);
		
		return entity;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(T entity) {
		getDao().delete(entity);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteAll() {
		return getDao().deleteAll();
	}
}
	
	public class FullTextIndexService extends AbstractGenericService implements IFullTextIndexService  {}
	
	public class OrderFullTextIndexService extends AbstractGenericService	implements IOrderFullTextIndexService {}
	
	public class OrderService extends AbstractGenericService implements IOrderService {}
*/