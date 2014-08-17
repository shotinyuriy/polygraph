package kz.aksay.polygraph.dao;

import org.springframework.stereotype.Repository;

import kz.aksay.polygraph.entity.Employee;

@Repository
public class EmployeeDao extends GenericDaoHibernateImpl<Employee, Long> {

}
