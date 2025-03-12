
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightRepository;
import acme.entities.legs.Leg;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctPublishingPolicy = true;
			if (!flight.isDraftMode()) {
				boolean areLegsPublished = false;
				List<Leg> legs = this.repository.findAllLegs(flight.getId());
				if (legs != null) {
					areLegsPublished = true;
					for (Leg leg : legs)
						if (leg.isDraftMode()) {
							areLegsPublished = false;
							break;
						}
				}
				correctPublishingPolicy = areLegsPublished;
			}

			super.state(context, correctPublishingPolicy, "flightNumber", "acme.validation.flight.correctPublishingPolitic.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
