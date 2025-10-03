
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.customer.id = :customerId")
	Collection<BookingRecord> findBookingRecordsByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT br FROM BookingRecord br WHERE br.id = :masterId")
	BookingRecord findBookingRecordById(@Param("masterId") int masterId);

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
	Collection<Booking> findAllBookingsByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :customerId")
	Collection<Passenger> findAllPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :bookingId AND br.passenger.id = :passengerId")
	BookingRecord findByBookingIdAndPassengerId(int bookingId, int passengerId);

	@Query("select b from Booking b where b.customer.id = :customerId and b.draftMode = true")
	Collection<Booking> findDraftBookingsByCustomerId(int customerId);

}
