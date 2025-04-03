
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.activityLogs.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (activityLog == null || activityLog.getFlightAssignment() == null || activityLog.getFlightAssignment().getLeg() == null || activityLog.getFlightAssignment().getLeg().getScheduledArrival() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		else {
			boolean registrationMomentIsValid = activityLog.getRegistrationMoment() != null;
			super.state(context, registrationMomentIsValid, "registrationMoment", "acme.validation.activitylog.registrationmoment.required");

			if (registrationMomentIsValid) {
				boolean registrationMomentIsAfterArrivalLeg;
				Date minRegistrationMoment = new Date(activityLog.getFlightAssignment().getLeg().getScheduledArrival().getTime());
				registrationMomentIsAfterArrivalLeg = MomentHelper.isAfterOrEqual(activityLog.getRegistrationMoment(), minRegistrationMoment);
				super.state(context, registrationMomentIsAfterArrivalLeg, "registrationMoment", "acme.validation.activitylog.registrationmoment.message");
			}
		}
		result = !super.hasErrors(context);
		return result;
	}

}
