
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueLeg;
				Leg existingLeg;

				existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				uniqueLeg = existingLeg == null || existingLeg.equals(leg);

				super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flightNumber.message");
			}
			{
				boolean correctFlightNumber = true;
				if (leg.getFlightNumber() == null)
					correctFlightNumber = false;
				for (int i = 0; i < 3; i++)
					if (correctFlightNumber == true)
						correctFlightNumber = leg.getFlightNumber().charAt(i) == leg.getFlight().getManager().getAirline().getIATACode().charAt(i);
					else
						i = 3;

				super.state(context, correctFlightNumber, "*", "acme.validation.leg.flightNumber.message");
			}
			{
				boolean arrivalAfterDeparture;

				arrivalAfterDeparture = leg.getScheduledArrival().after(leg.getScheduledDeparture());

				super.state(context, arrivalAfterDeparture, "*", "acme.validation.leg.scheduledArrival-Departure.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
