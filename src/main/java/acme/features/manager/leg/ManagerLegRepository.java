
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.managers.Manager;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.manager.id = :managerId")
	Collection<Leg> findLegsByManagerId(int managerId);

	@Query("select l from Leg l where l.id = :masterId")
	Leg findLegById(int masterId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("select f from Flight f where f.id = :masterId")
	Flight findFlightById(int masterId);

	@Query("select m from Manager m where m.id = :masterId")
	Manager findManagerById(int masterId);

	@Query("select a from Aircraft a where a.airline.id = :airlineId")
	Collection<Aircraft> findAircraftsByAirlineId(int airlineId);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select l from Leg l where l.flight.id = :masterId")
	List<Leg> findAllLegsByFlightId(int masterId);
}
