
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	// Repositorios necesarios
	@Autowired
	private BookingRepository bookingRepository;


	// Inicialización de la anotación de validación
	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		// Verificar si el booking está en borrador
		if (booking.isDraftMode() == false) {

			// Verificar si hay al menos un pasajero asociado
			int passengerCount = this.bookingRepository.countPassengersByLocatorCode(booking.getLocatorCode());
			boolean hasPassengers = passengerCount > 0;
			super.state(context, hasPassengers, "*", "acme.validation.booking.passenger-required.message");

			// Verificar si el último nibble de la tarjeta ha sido almacenado
			boolean hasCardNibble = booking.getLastCardNibble() != null && !booking.getLastCardNibble().isEmpty();
			super.state(context, hasCardNibble, "*", "acme.validation.booking.card-nibble-required.message");
		}

		// Verificar si no hay errores de validación
		result = !super.hasErrors(context);
		return result;
	}
}
