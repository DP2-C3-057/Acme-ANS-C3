
package acme.entities.activityLogs;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

public interface ActivityLogRepository extends AbstractRepository {

	@Query("SELECT FA.leg FROM ActivityLog AL JOIN AL.flightAssignment FA WHERE AL.id = :activityLogId")
	Leg findLegByActivityLogId(int activityLogId);

}
