
package acme.features.manager.managerDashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;
import acme.realms.managers.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select m from Manager m where m.id = :managerId")
	Manager getManagerById(int managerId);

	@Query("select count(m) from Manager m where m.experienceYears > :experience")
	int moreExperienceYearsThan(int experience);

	@Query("select a, count(l) from Leg l join Airport a on a = l.departureAirport or a = l.arrivalAirport where l.flight.manager.id = :managerId group by a order by count(l) desc")
	List<Airport> airportPopularity(int managerId);

	@Query("select count(l) from Leg l where l.flight.manager.id = :managerId and l.status = acme.entities.legs.LegStatus.ON_TIME")
	int onTimeLegs(int managerId);

	@Query("select count(l) from Leg l where l.flight.manager.id = :managerId and l.status = acme.entities.legs.LegStatus.DELAYED")
	int delayedLegs(int managerId);

	@Query("select count(l) from Leg l where l.flight.manager.id = :managerId and l.status = acme.entities.legs.LegStatus.CANCELLED")
	int cancelledLegs(int managerId);

	@Query("select count(l) from Leg l where l.flight.manager.id = :managerId and l.status = acme.entities.legs.LegStatus.LANDED")
	int landedLegs(int managerId);

	@Query("select avg(f.cost.amount) from Flight f where f.manager.id = :managerId")
	Double averageCostFlight(int managerId);

	@Query("select min(f.cost.amount) from Flight f where f.manager.id = :managerId")
	Double minimumCostFlight(int managerId);

	@Query("select max(f.cost.amount) from Flight f where f.manager.id = :managerId")
	Double maximumCostFlight(int managerId);

	@Query("select stddev(f.cost.amount) FROM Flight f WHERE f.manager.id = :managerId")
	Double standardDeviationCost(int managerId);

}
