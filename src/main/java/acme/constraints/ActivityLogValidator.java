
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.activityLogs.ActivityLogRepository;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Autowired
	private ActivityLogRepository acrepository;


	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (activityLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean validLeg;
			Leg existingLeg;

			existingLeg = this.acrepository.findLegByActivityLogId(activityLog.getId());
			validLeg = existingLeg.getStatus() == LegStatus.LANDED;

			super.state(context, validLeg, "leg", "acme.validation.activityLog.invalidLeg");
		}
		result = !super.hasErrors(context);
		return result;
	}

}
