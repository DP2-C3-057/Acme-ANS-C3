
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.flights.Flight;
import acme.entities.passengers.Passenger;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.id = :masterId")
	Booking findBookingById(int masterId);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode")
	Booking findByLocatorCode(String locatorCode);

	@Query("select p from Passenger p")
	Collection<Passenger> findAllPassengers();

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.customer.id = :customerId")
	Collection<Passenger> findPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false")
	Collection<Flight> findAllFlightsDraftModeFalse();

	@Modifying
	@Query("DELETE FROM BookingRecord br WHERE br.booking.id = :bookingId")
	void deleteBookingRecordsByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :id")
	Collection<BookingRecord> findBookingRecordsByBookingId(int id);

}
