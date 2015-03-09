package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.Person;

import org.springframework.stereotype.Service;

@Service
public interface IPersonService extends IGenericService<Person, Long> {
	public Person findSimilarPerson(final Person person);
	
}
