package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.User;

public interface IUserService extends IGenericService<User, Long> {
	public User findByLogin(String login);
	public User findByLoginAndPassword(String login, String password);	
	public User findByEmployee(Employee employee);
}
