
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM ActivityLog al")
	Collection<ActivityLog> findAllActivityLogs();

	@Query("select al from ActivityLog al where al.flightAssignment.id = ?1")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("select f from FlightAssignment f where f.id = ?1")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT al FROM ActivityLog al WHERE al.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("SELECT fa FROM FlightAssignment fa")
	Collection<FlightAssignment> findAllFlightAssignments();
}
