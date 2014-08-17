package kz.aksay.polygraph.dao;

import kz.aksay.polygraph.entity.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenericDaoHibernateImpl<User, Long> {

}
