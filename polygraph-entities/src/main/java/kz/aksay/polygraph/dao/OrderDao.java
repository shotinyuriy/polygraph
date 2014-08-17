package kz.aksay.polygraph.dao;

import org.springframework.stereotype.Repository;

import kz.aksay.polygraph.entity.Order;

@Repository
public class OrderDao extends GenericDaoHibernateImpl<Order, Long> {

}
