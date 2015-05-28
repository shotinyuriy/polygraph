package kz.aksay.polygraph.api;

import java.util.List;

import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.VicariousPower;

public interface IVicariousPowerService extends IGenericService<VicariousPower, Long> {

	List<VicariousPower> findByOrganization(Organization organization);

}
