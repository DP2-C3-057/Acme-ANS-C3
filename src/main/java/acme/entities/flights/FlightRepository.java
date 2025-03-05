
package acme.entities.flights;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduledDeparture) from Leg where l.flight.id==flightId")
	Date findScheduledDeparture(int flightId);

	@Query("select max(l.scheduledArrival) from Leg where l.flight.id==flightId")
	Date findScheduledArrival(int flightId);
}
