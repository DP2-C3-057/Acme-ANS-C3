
package acme.realms.assistanceAgents;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("select a from AssistanceAgent a where a.employeeCode = :employeeCode")
	AssistanceAgent findAssitanceAgentByEmployeeCode(String employeeCode);
}
