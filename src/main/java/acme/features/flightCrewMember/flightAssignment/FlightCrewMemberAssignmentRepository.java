
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface FlightCrewMemberAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.leg.scheduledArrival < :now and f.flightCrewMember.id = :memberId")
	Collection<FlightAssignment> assignmentsWithCompletedLegs(@Param("memberId") Integer memberId, @Param("now") Date now);

	@Query("select f from FlightAssignment f where f.leg.scheduledDeparture > :now and f.flightCrewMember.id = :memberId")
	Collection<FlightAssignment> assignmentsWithPlannedLegs(@Param("memberId") Integer memberId, @Param("now") Date now);

	@Query("select f from FlightAssignment f where f.id = ?1")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from Leg l where l.id = ?1")
	Leg findLegById(int id);

	@Query("select f.leg from FlightAssignment f where f.flightCrewMember.id = ?1")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId);

	@Query("SELECT l FROM Leg l where l.draftMode = false")
	Collection<Leg> findAllLegsPublish();

	@Query("SELECT fcm FROM FlightCrewMember fcm")
	Collection<FlightCrewMember> findAllFlightCrewMembers();

	@Query("select al from ActivityLog al where al.flightAssignment.id = ?1")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("SELECT fcm FROM FlightCrewMember fcm WHERE fcm.id = ?1")
	FlightCrewMember findFlightCrewMemberById(int flightCrewMemberId);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE (fa.leg.scheduledDeparture < :arrival AND fa.leg.scheduledArrival > :departure) AND fa.leg.id <> :legId AND fa.flightCrewMember.id = :flightCrewMemberId")
	List<Leg> findSimultaneousLegsByMemberId(Date departure, Date arrival, int legId, int flightCrewMemberId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.draftMode = false and fa.leg = :flightAssignmentLeg and fa.duty = :duty")
	Collection<FlightAssignment> findFlightAssignmentByLegAndDuty(Leg flightAssignmentLeg, Duty duty);

}
