
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctPercentaje = true;
			if (trackingLog.getIndicator() == TrackingLogStatus.ACCEPTED || trackingLog.getIndicator() == TrackingLogStatus.REJECTED)
				if (trackingLog.getResolutionPercentage() != 100)
					correctPercentaje = false;

			super.state(context, correctPercentaje, "*", "acme.validation.trackingLog.correctPercentaje.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
