package kz.aksay.polygraph.dao;

import org.springframework.stereotype.Repository;

import kz.aksay.polygraph.entity.Person;

@Repository
public class PersonDao extends GenericDaoHibernateImpl<Person, Long> {

}
