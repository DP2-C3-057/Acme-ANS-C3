
package acme.entities.bookings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("select count(br.passenger) from BookingRecord br where br.booking.locatorCode = :locatorCode")
	Integer countPassengersByLocatorCode(String locatorCode);

}
