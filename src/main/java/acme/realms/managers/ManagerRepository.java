
package acme.realms.managers;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerRepository extends AbstractRepository {

	@Query("select m from Manager m where m.idNumber = :idNumber")
	Manager findManagerByIdNumber(String idNumber);
}
