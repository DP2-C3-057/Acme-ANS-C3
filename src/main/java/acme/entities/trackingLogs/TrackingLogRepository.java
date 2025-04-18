
package acme.entities.trackingLogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.realms.assistanceAgents.AssistanceAgent;

public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim = :claimId")
	List<TrackingLog> findTrackingLogByClaimId(Claim claimId);

	@Query("select distinct t.claim from TrackingLog t")
	List<Claim> findAllClaimsWithTrackingLogs();

	@Query("select c.assistanceAgent from Claim c where c = :claim")
	AssistanceAgent findAgentByClaim(Claim claim);

}
